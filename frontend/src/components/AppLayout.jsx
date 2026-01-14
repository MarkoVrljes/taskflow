import { NavLink, Outlet } from 'react-router-dom'
import { useAuth } from '../state/AuthContext.jsx'

export default function AppLayout() {
  const { logout } = useAuth()

  return (
    <div className="app-shell">
      <div className="topbar">
        <div className="brand">
          <div className="brand-mark">T</div>
          <div>
            <h1>Taskflow</h1>
            <p className="tagline">Organize work. Ship faster.</p>
          </div>
        </div>
        <div className="top-actions">
          <NavLink className="pill" to="/workspaces">
            Workspaces
          </NavLink>
          <button className="button secondary" onClick={logout}>
            Sign out
          </button>
        </div>
      </div>
      <Outlet />
    </div>
  )
}
