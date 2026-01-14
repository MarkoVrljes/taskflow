import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../state/AuthContext.jsx'

export default function WorkspacesPage() {
  const { apiRequest } = useAuth()
  const [workspaces, setWorkspaces] = useState([])
  const [name, setName] = useState('')
  const [error, setError] = useState('')

  const loadWorkspaces = async () => {
    const data = await apiRequest('/workspaces')
    setWorkspaces(data)
  }

  useEffect(() => {
    loadWorkspaces().catch((err) => setError(err.message))
  }, [])

  const handleCreate = async () => {
    if (!name.trim()) return
    try {
      await apiRequest('/workspaces', {
        method: 'POST',
        body: JSON.stringify({ name: name.trim() }),
      })
      setName('')
      await loadWorkspaces()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h2 className="panel-title">Workspaces</h2>
          <p className="muted">Select a workspace to manage projects and tasks.</p>
        </div>
      </div>
      {error && <div className="alert">{error}</div>}
      <div className="layout">
        <div className="panel">
          <div className="list">
            {workspaces.length === 0 && <div className="empty">No workspaces yet.</div>}
            {workspaces.map((workspace) => (
              <Link key={workspace.id} to={`/workspaces/${workspace.id}`} className="list-item">
                <strong>{workspace.name}</strong>
                <span className="muted">{workspace.id}</span>
              </Link>
            ))}
          </div>
        </div>
        <div className="panel">
          <div className="panel-header">
            <h3 className="panel-title">Create workspace</h3>
          </div>
          <div className="form">
            <input
              className="input"
              value={name}
              onChange={(event) => setName(event.target.value)}
              placeholder="Workspace name"
            />
            <button className="button" type="button" onClick={handleCreate}>
              Add workspace
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
