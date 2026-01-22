import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '../state/AuthContext.jsx'

export default function LoginPage() {
  const { login, register } = useAuth()
  const [mode, setMode] = useState('login')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [rememberMe, setRememberMe] = useState(true)
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    try {
      if (mode === 'login') {
        await login(email, password, rememberMe)
      } else {
        await register(email, password, rememberMe)
      }
      navigate('/workspaces')
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="auth-card">
      <div className="brand">
        <div className="brand-mark">T</div>
        <div>
          <h1>Taskflow</h1>
          <p className="tagline">Team task manager</p>
        </div>
      </div>
      {error && <div className="alert">{error}</div>}
      <form className="form" onSubmit={handleSubmit}>
        <div className="form-row">
          <label>Email</label>
          <input
            className="input"
            value={email}
            onChange={(event) => setEmail(event.target.value)}
            placeholder="you@company.com"
            type="email"
            required
          />
        </div>
        <div className="form-row">
          <label>Password</label>
          <input
            className="input"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            type="password"
            required
          />
        </div>
        <div className="form-row checkbox-row">
          <label>
            <input
              type="checkbox"
              checked={rememberMe}
              onChange={(event) => setRememberMe(event.target.checked)}
            />
            Remember me
          </label>
        </div>
        <div className="auth-actions">
          <button className="button" type="submit">
            {mode === 'login' ? 'Sign in' : 'Create account'}
          </button>
          <button
            className="button secondary"
            type="button"
            onClick={() => setMode(mode === 'login' ? 'register' : 'login')}
          >
            {mode === 'login' ? 'Need an account' : 'Have an account'}
          </button>
        </div>
      </form>
    </div>
  )
}
