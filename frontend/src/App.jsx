import { useEffect, useMemo, useState } from 'react'
import './App.css'

function App() {
  const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'
  const [authMode, setAuthMode] = useState('login')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [auth, setAuth] = useState(() => ({
    accessToken: localStorage.getItem('accessToken') || '',
    refreshToken: localStorage.getItem('refreshToken') || '',
  }))
  const [error, setError] = useState('')
  const [workspaces, setWorkspaces] = useState([])
  const [selectedWorkspaceId, setSelectedWorkspaceId] = useState('')
  const [workspaceName, setWorkspaceName] = useState('')
  const [projects, setProjects] = useState([])
  const [selectedProjectId, setSelectedProjectId] = useState('')
  const [projectName, setProjectName] = useState('')
  const [projectDescription, setProjectDescription] = useState('')
  const [tasks, setTasks] = useState([])
  const [taskTitle, setTaskTitle] = useState('')
  const [taskDescription, setTaskDescription] = useState('')
  const [taskPriority, setTaskPriority] = useState('MED')
  const [taskStatus, setTaskStatus] = useState('TODO')
  const [filterStatus, setFilterStatus] = useState('ALL')
  const [filterPriority, setFilterPriority] = useState('ALL')
  const [taskQuery, setTaskQuery] = useState('')
  const [filterAssignee, setFilterAssignee] = useState('')
  const [taskAssignee, setTaskAssignee] = useState('')
  const [taskPage, setTaskPage] = useState(0)
  const [taskSize, setTaskSize] = useState(10)
  const [taskMeta, setTaskMeta] = useState({ page: 0, totalPages: 1, total: 0 })
  const [selectedTask, setSelectedTask] = useState(null)
  const [comments, setComments] = useState([])
  const [commentBody, setCommentBody] = useState('')
  const [inviteEmail, setInviteEmail] = useState('')
  const [inviteRole, setInviteRole] = useState('MEMBER')
  const [inviteToken, setInviteToken] = useState('')

  const isAuthed = Boolean(auth.accessToken)

  const visibleTasks = useMemo(() => {
    if (!selectedProjectId) {
      return tasks
    }
    return tasks.filter((task) => task.projectId === selectedProjectId)
  }, [tasks, selectedProjectId])

  const saveAuth = (data) => {
    localStorage.setItem('accessToken', data.accessToken || '')
    localStorage.setItem('refreshToken', data.refreshToken || '')
    setAuth({
      accessToken: data.accessToken || '',
      refreshToken: data.refreshToken || '',
    })
  }

  const clearAuth = () => {
    localStorage.removeItem('accessToken')
    localStorage.removeItem('refreshToken')
    setAuth({ accessToken: '', refreshToken: '' })
    setWorkspaces([])
    setProjects([])
    setTasks([])
    setSelectedWorkspaceId('')
    setSelectedProjectId('')
    setSelectedTask(null)
    setComments([])
  }

  const apiRequest = async (path, options = {}, allowRefresh = true) => {
    const headers = {
      'Content-Type': 'application/json',
      ...(options.headers || {}),
    }
    if (auth.accessToken) {
      headers.Authorization = `Bearer ${auth.accessToken}`
    }
    const response = await fetch(`${API_BASE}${path}`, {
      ...options,
      headers,
    })

    if (response.status === 401 && auth.refreshToken && allowRefresh) {
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
  }

  const refreshTokens = async () => {
    try {
      const response = await fetch(`${API_BASE}/auth/refresh`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ refreshToken: auth.refreshToken }),
      })
      if (!response.ok) {
        clearAuth()
        return false
      }
      const data = await response.json()
      saveAuth(data)
      return true
    } catch {
      clearAuth()
      return false
    }
  }

  const loadWorkspaces = async () => {
    const data = await apiRequest('/workspaces')
    setWorkspaces(data)
    if (!selectedWorkspaceId && data.length) {
      setSelectedWorkspaceId(data[0].id)
    }
    if (!data.length) {
      setSelectedWorkspaceId('')
    }
  }

  const loadProjects = async (workspaceId) => {
    if (!workspaceId) return
    const data = await apiRequest(`/workspaces/${workspaceId}/projects`)
    setProjects(data)
    if (!selectedProjectId && data.length) {
      setSelectedProjectId(data[0].id)
    }
    if (!data.length) {
      setSelectedProjectId('')
    }
  }

  const loadTasks = async (workspaceId) => {
    if (!workspaceId) return
    const params = new URLSearchParams()
    if (filterStatus !== 'ALL') params.set('status', filterStatus)
    if (filterPriority !== 'ALL') params.set('priority', filterPriority)
    if (filterAssignee) params.set('assigneeId', filterAssignee)
    if (taskQuery) params.set('q', taskQuery)
    params.set('page', taskPage.toString())
    params.set('size', taskSize.toString())
    params.set('sort', 'createdAt,desc')
    const data = await apiRequest(`/workspaces/${workspaceId}/tasks?${params.toString()}`)
    setTasks(data.content || [])
    setTaskMeta({
      page: data.number || 0,
      totalPages: data.totalPages || 1,
      total: data.totalElements || 0,
    })
  }

  const loadComments = async (taskId) => {
    if (!taskId) return
    const data = await apiRequest(`/tasks/${taskId}/comments`)
    setComments(data)
  }

  useEffect(() => {
    setError('')
    if (isAuthed) {
      loadWorkspaces().catch((err) => setError(err.message))
    }
  }, [isAuthed])

  useEffect(() => {
    if (selectedWorkspaceId) {
      loadProjects(selectedWorkspaceId).catch((err) => setError(err.message))
      loadTasks(selectedWorkspaceId).catch((err) => setError(err.message))
    }
  }, [selectedWorkspaceId, filterStatus, filterPriority, taskQuery, filterAssignee, taskPage, taskSize])

  useEffect(() => {
    setTaskPage(0)
  }, [filterStatus, filterPriority, taskQuery, filterAssignee])

  useEffect(() => {
    setInviteToken('')
  }, [selectedWorkspaceId])

  useEffect(() => {
    if (selectedTask) {
      loadComments(selectedTask.id).catch((err) => setError(err.message))
    }
  }, [selectedTask])

  useEffect(() => {
    setSelectedTask(null)
    setComments([])
  }, [selectedWorkspaceId, selectedProjectId])

  const handleAuth = async (event) => {
    event.preventDefault()
    setError('')
    try {
      const data = await apiRequest(`/auth/${authMode}`, {
        method: 'POST',
        body: JSON.stringify({ email, password }),
      })
      saveAuth(data)
      setEmail('')
      setPassword('')
    } catch (err) {
      setError(err.message)
    }
  }

  const handleLogout = async () => {
    try {
      await apiRequest('/auth/logout', { method: 'POST' }, false)
    } catch {
      // ignore
    }
    clearAuth()
  }

  const handleCreateWorkspace = async () => {
    if (!workspaceName.trim()) return
    try {
      const data = await apiRequest('/workspaces', {
        method: 'POST',
        body: JSON.stringify({ name: workspaceName.trim() }),
      })
      setWorkspaceName('')
      await loadWorkspaces()
      setSelectedWorkspaceId(data.id)
    } catch (err) {
      setError(err.message)
    }
  }

  const handleCreateProject = async () => {
    if (!selectedWorkspaceId || !projectName.trim()) return
    try {
      const data = await apiRequest(`/workspaces/${selectedWorkspaceId}/projects`, {
        method: 'POST',
        body: JSON.stringify({
          name: projectName.trim(),
          description: projectDescription.trim(),
        }),
      })
      setProjectName('')
      setProjectDescription('')
      await loadProjects(selectedWorkspaceId)
      setSelectedProjectId(data.id)
    } catch (err) {
      setError(err.message)
    }
  }

  const handleCreateTask = async () => {
    if (!selectedProjectId || !taskTitle.trim()) return
    try {
      await apiRequest(`/projects/${selectedProjectId}/tasks`, {
        method: 'POST',
        body: JSON.stringify({
          title: taskTitle.trim(),
          description: taskDescription.trim(),
          priority: taskPriority,
          status: taskStatus,
          assigneeId: taskAssignee || null,
        }),
      })
      setTaskTitle('')
      setTaskDescription('')
      setTaskAssignee('')
      await loadTasks(selectedWorkspaceId)
    } catch (err) {
      setError(err.message)
    }
  }

  const handleAddComment = async () => {
    if (!selectedTask || !commentBody.trim()) return
    try {
      await apiRequest(`/tasks/${selectedTask.id}/comments`, {
        method: 'POST',
        body: JSON.stringify({ body: commentBody.trim() }),
      })
      setCommentBody('')
      await loadComments(selectedTask.id)
    } catch (err) {
      setError(err.message)
    }
  }

  const handleInvite = async () => {
    if (!selectedWorkspaceId || !inviteEmail.trim()) return
    try {
      const data = await apiRequest(`/workspaces/${selectedWorkspaceId}/invites`, {
        method: 'POST',
        body: JSON.stringify({ email: inviteEmail.trim(), role: inviteRole }),
      })
      setInviteToken(data.token)
      setInviteEmail('')
    } catch (err) {
      setError(err.message)
    }
  }

  if (!isAuthed) {
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
        <form className="form" onSubmit={handleAuth}>
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
          <div className="auth-actions">
            <button className="button" type="submit">
              {authMode === 'login' ? 'Sign in' : 'Create account'}
            </button>
            <button
              className="button secondary"
              type="button"
              onClick={() => setAuthMode(authMode === 'login' ? 'register' : 'login')}
            >
              {authMode === 'login' ? 'Need an account' : 'Have an account'}
            </button>
          </div>
        </form>
      </div>
    )
  }

  return (
    <div className="app-shell">
      <div className="topbar">
        <div className="brand">
          <div className="brand-mark">T</div>
          <div>
            <h1>Taskflow</h1>
            <p className="tagline">Workspaces, projects, tasks</p>
          </div>
        </div>
        <div className="top-actions">
          <span className="pill">API {API_BASE}</span>
          <button className="button secondary" onClick={handleLogout}>
            Sign out
          </button>
        </div>
      </div>

      {error && <div className="alert">{error}</div>}

      <div className="layout">
        <div className="panel">
          <div className="panel-header">
            <h2 className="panel-title">Workspaces</h2>
          </div>
          <div className="list">
            {workspaces.length === 0 && <div className="empty">No workspaces yet.</div>}
            {workspaces.map((workspace) => (
              <div
                key={workspace.id}
                className={`list-item ${workspace.id === selectedWorkspaceId ? 'active' : ''}`}
                onClick={() => {
                  setSelectedWorkspaceId(workspace.id)
                  setSelectedProjectId('')
                }}
              >
                <strong>{workspace.name}</strong>
                <span className="muted">{workspace.id}</span>
              </div>
            ))}
          </div>
          <div className="form">
            <input
              className="input"
              value={workspaceName}
              onChange={(event) => setWorkspaceName(event.target.value)}
              placeholder="New workspace name"
            />
            <button className="button" type="button" onClick={handleCreateWorkspace}>
              Add workspace
            </button>
          </div>
          <div className="form">
            <div className="panel-header">
              <h3 className="panel-title">Invite member</h3>
            </div>
            <input
              className="input"
              value={inviteEmail}
              onChange={(event) => setInviteEmail(event.target.value)}
              placeholder="Invite email"
            />
            <select
              className="select"
              value={inviteRole}
              onChange={(event) => setInviteRole(event.target.value)}
            >
              <option value="ADMIN">ADMIN</option>
              <option value="MEMBER">MEMBER</option>
              <option value="VIEWER">VIEWER</option>
            </select>
            <button className="button secondary" type="button" onClick={handleInvite}>
              Generate invite
            </button>
            {inviteToken && (
              <div className="alert">
                Invite token: <strong>{inviteToken}</strong>
              </div>
            )}
          </div>
        </div>

        <div className="panel">
          <div className="panel-header">
            <h2 className="panel-title">Projects</h2>
          </div>
          <div className="list">
            {projects.length === 0 && <div className="empty">No projects yet.</div>}
            {projects.map((project) => (
              <div
                key={project.id}
                className={`list-item ${project.id === selectedProjectId ? 'active' : ''}`}
                onClick={() => setSelectedProjectId(project.id)}
              >
                <strong>{project.name}</strong>
                {project.description && <span className="muted">{project.description}</span>}
              </div>
            ))}
          </div>
          <div className="form">
            <input
              className="input"
              value={projectName}
              onChange={(event) => setProjectName(event.target.value)}
              placeholder="Project name"
            />
            <input
              className="input"
              value={projectDescription}
              onChange={(event) => setProjectDescription(event.target.value)}
              placeholder="Short description"
            />
            <button className="button" type="button" onClick={handleCreateProject}>
              Add project
            </button>
          </div>
        </div>

        <div className="panel">
          <div className="panel-header">
            <h2 className="panel-title">Tasks</h2>
          </div>
          <div className="filters">
            <select className="select" value={filterStatus} onChange={(event) => setFilterStatus(event.target.value)}>
              <option value="ALL">ALL</option>
              <option value="TODO">TODO</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="DONE">DONE</option>
            </select>
            <select
              className="select"
              value={filterPriority}
              onChange={(event) => setFilterPriority(event.target.value)}
            >
              <option value="ALL">ALL</option>
              <option value="LOW">LOW</option>
              <option value="MED">MED</option>
              <option value="HIGH">HIGH</option>
            </select>
            <input
              className="input"
              value={taskQuery}
              onChange={(event) => setTaskQuery(event.target.value)}
              placeholder="Search title or description"
            />
            <input
              className="input"
              value={filterAssignee}
              onChange={(event) => setFilterAssignee(event.target.value)}
              placeholder="Assignee ID (optional)"
            />
          </div>

          <div className="tasks">
            {visibleTasks.length === 0 && <div className="empty">No tasks found.</div>}
            {visibleTasks.map((task) => (
              <div key={task.id} className="task-card" onClick={() => setSelectedTask(task)}>
                <strong>{task.title}</strong>
                <div className="muted">{task.description || 'No description'}</div>
                <div className="split">
                  <span className="badge">{task.status}</span>
                  <span className="badge">{task.priority}</span>
                </div>
              </div>
            ))}
          </div>
          <div className="top-actions">
            <span className="pill">
              Page {taskMeta.page + 1} of {taskMeta.totalPages}
            </span>
            <button
              className="button ghost"
              type="button"
              onClick={() => setTaskPage(Math.max(taskMeta.page - 1, 0))}
              disabled={taskMeta.page === 0}
            >
              Prev
            </button>
            <button
              className="button ghost"
              type="button"
              onClick={() => setTaskPage(Math.min(taskMeta.page + 1, taskMeta.totalPages - 1))}
              disabled={taskMeta.page >= taskMeta.totalPages - 1}
            >
              Next
            </button>
          </div>

          {!selectedProjectId && <div className="alert">Select a project to add tasks.</div>}

          <div className="form">
            <input
              className="input"
              value={taskTitle}
              onChange={(event) => setTaskTitle(event.target.value)}
              placeholder="Task title"
            />
            <textarea
              className="textarea"
              value={taskDescription}
              onChange={(event) => setTaskDescription(event.target.value)}
              placeholder="Task details"
            />
            <input
              className="input"
              value={taskAssignee}
              onChange={(event) => setTaskAssignee(event.target.value)}
              placeholder="Assignee ID (optional)"
            />
            <div className="filters">
              <select className="select" value={taskStatus} onChange={(event) => setTaskStatus(event.target.value)}>
                <option value="TODO">TODO</option>
                <option value="IN_PROGRESS">IN_PROGRESS</option>
                <option value="DONE">DONE</option>
              </select>
              <select
                className="select"
                value={taskPriority}
                onChange={(event) => setTaskPriority(event.target.value)}
              >
                <option value="LOW">LOW</option>
                <option value="MED">MED</option>
                <option value="HIGH">HIGH</option>
              </select>
            </div>
            <button className="button" type="button" onClick={handleCreateTask}>
              Add task
            </button>
          </div>

          <div className="panel">
            <div className="panel-header">
              <h3 className="panel-title">Task detail</h3>
            </div>
            {!selectedTask && <div className="empty">Select a task to view details.</div>}
            {selectedTask && (
              <div className="form">
                <div>
                  <strong>{selectedTask.title}</strong>
                  <p className="muted">{selectedTask.description || 'No description'}</p>
                </div>
                <div className="split">
                  <span className="badge">Status: {selectedTask.status}</span>
                  <span className="badge">Priority: {selectedTask.priority}</span>
                </div>
                <div className="form-row">
                  <label>Add comment</label>
                  <textarea
                    className="textarea"
                    value={commentBody}
                    onChange={(event) => setCommentBody(event.target.value)}
                    placeholder="Write a comment"
                  />
                  <button className="button secondary" type="button" onClick={handleAddComment}>
                    Post comment
                  </button>
                </div>
                <div className="list">
                  {comments.length === 0 && <div className="empty">No comments yet.</div>}
                  {comments.map((comment) => (
                    <div key={comment.id} className="list-item">
                      <div className="muted">{comment.authorId}</div>
                      <div>{comment.body}</div>
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  )
}

export default App
