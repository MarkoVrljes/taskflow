import { createContext, useCallback, useContext, useMemo, useState } from 'react'

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const AuthContext = createContext(null)

const readStoredTokens = () => {
  const local = {
    accessToken: localStorage.getItem('accessToken') || '',
    refreshToken: localStorage.getItem('refreshToken') || '',
  }
  if (local.accessToken || local.refreshToken) {
    return { tokens: local, storage: 'local' }
  }
  const session = {
    accessToken: sessionStorage.getItem('accessToken') || '',
    refreshToken: sessionStorage.getItem('refreshToken') || '',
  }
  if (session.accessToken || session.refreshToken) {
    return { tokens: session, storage: 'session' }
  }
  return { tokens: { accessToken: '', refreshToken: '' }, storage: null }
}

export function AuthProvider({ children }) {
  const initial = readStoredTokens()
  const [tokens, setTokensState] = useState(initial.tokens)
  const [tokenStorage, setTokenStorage] = useState(initial.storage)
  const [error, setError] = useState('')

  const persistTokens = useCallback((next, storage) => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    sessionStorage.removeItem('accessToken')
    sessionStorage.removeItem('refreshToken')
    const target = storage === 'session' ? sessionStorage : localStorage
    target.setItem('accessToken', next.accessToken)
    target.setItem('refreshToken', next.refreshToken)
    setTokensState(next)
    setTokenStorage(storage)
  }, [])

  const setTokens = useCallback((data, rememberMe = true) => {
    const next = {
      accessToken: data?.accessToken || '',
      refreshToken: data?.refreshToken || '',
    }
    const storage = rememberMe ? 'local' : 'session'
    persistTokens(next, storage)
  }, [persistTokens])

  const clearTokens = useCallback(() => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    sessionStorage.removeItem('accessToken')
    sessionStorage.removeItem('refreshToken')
    setTokensState({ accessToken: '', refreshToken: '' })
    setTokenStorage(null)
  }, [])

  const refreshTokens = useCallback(async () => {
    if (!tokens.refreshToken) {
      return false
    }
    try {
      const response = await fetch(`${API_BASE}/auth/refresh`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ refreshToken: tokens.refreshToken }),
      })
      if (!response.ok) {
        clearTokens()
        return false
      }
      const data = await response.json()
      const storage =
        tokenStorage || (localStorage.getItem('refreshToken') ? 'local' : 'session')
      persistTokens(data, storage || 'local')
      return true
    } catch {
      clearTokens()
      return false
    }
  }, [clearTokens, persistTokens, tokenStorage, tokens.refreshToken])

  const apiRequest = useCallback(
    async (path, options = {}, allowRefresh = true) => {
      const headers = {
        'Content-Type': 'application/json',
        ...(options.headers || {}),
      }
      if (tokens.accessToken) {
        headers.Authorization = `Bearer ${tokens.accessToken}`
      }
      const response = await fetch(`${API_BASE}${path}`, {
        ...options,
        headers,
      })
      if (response.status === 401 && allowRefresh) {
        const refreshed = await refreshTokens()
        if (refreshed) {
          return apiRequest(path, options, false)
        }
      }
      if (!response.ok) {
        let payload = null
        try {
          payload = await response.json()
        } catch {
          payload = null
        }
        const message = payload?.message || payload?.error || `Request failed (${response.status})`
        throw new Error(message)
      }
      if (response.status === 204) {
        return null
      }
      return response.json()
    },
    [refreshTokens, tokens.accessToken],
  )

  const login = useCallback(
    async (email, password, rememberMe = true) => {
      setError('')
      const data = await apiRequest('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      })
      setTokens(data, rememberMe)
      return data
    },
    [apiRequest, setTokens],
  )

  const register = useCallback(
    async (email, password, rememberMe = true) => {
      setError('')
      const data = await apiRequest('/auth/register', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      })
      setTokens(data, rememberMe)
      return data
    },
    [apiRequest, setTokens],
  )

  const logout = useCallback(async () => {
    try {
      await apiRequest('/auth/logout', { method: 'POST' }, false)
    } catch {
      // ignore
    }
    clearTokens()
  }, [apiRequest, clearTokens])

  const value = useMemo(
    () => ({
      tokens,
      isAuthed: Boolean(tokens.accessToken),
      apiRequest,
      login,
      register,
      logout,
      error,
      setError,
    }),
    [apiRequest, error, login, logout, register, tokens],
  )

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const ctx = useContext(AuthContext)
  if (!ctx) {
    throw new Error('useAuth must be used within AuthProvider')
  }
  return ctx
}
