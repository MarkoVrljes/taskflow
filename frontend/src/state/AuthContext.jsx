import { createContext, useCallback, useContext, useMemo, useState } from 'react'

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [tokens, setTokensState] = useState(() => ({
    accessToken: localStorage.getItem('accessToken') || '',
    refreshToken: localStorage.getItem('refreshToken') || '',
  }))
  const [error, setError] = useState('')

  const setTokens = useCallback((data) => {
    const next = {
      accessToken: data?.accessToken || '',
      refreshToken: data?.refreshToken || '',
    }
    localStorage.setItem('accessToken', next.accessToken)
    localStorage.setItem('refreshToken', next.refreshToken)
    setTokensState(next)
  }, [])

  const clearTokens = useCallback(() => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    setTokensState({ accessToken: '', refreshToken: '' })
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
      setTokens(data)
      return true
    } catch {
      clearTokens()
      return false
    }
  }, [clearTokens, setTokens, tokens.refreshToken])

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
    async (email, password) => {
      setError('')
      const data = await apiRequest('/auth/login', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      })
      setTokens(data)
      return data
    },
    [apiRequest, setTokens],
  )

  const register = useCallback(
    async (email, password) => {
      setError('')
      const data = await apiRequest('/auth/register', {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      })
      setTokens(data)
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
