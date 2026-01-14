import { useEffect, useMemo, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useAuth } from '../state/AuthContext.jsx'

export default function WorkspacePage() {
  const { workspaceId } = useParams()
  const { apiRequest } = useAuth()
  const [workspace, setWorkspace] = useState(null)
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
  const [filterAssignee, setFilterAssignee] = useState('')
  const [taskQuery, setTaskQuery] = useState('')
  const [inviteEmail, setInviteEmail] = useState('')
  const [inviteRole, setInviteRole] = useState('MEMBER')
  const [inviteToken, setInviteToken] = useState('')
  const [error, setError] = useState('')

  const visibleTasks = useMemo(() => {
    if (!selectedProjectId) {
      return tasks
    }
    return tasks.filter((task) => task.projectId === selectedProjectId)
  }, [selectedProjectId, tasks])

  const loadWorkspace = async () => {
    const data = await apiRequest(`/workspaces/${workspaceId}`)
    setWorkspace(data)
  }

  const loadProjects = async () => {
    const data = await apiRequest(`/workspaces/${workspaceId}/projects`)
    setProjects(data)
    if (!selectedProjectId && data.length) {
      setSelectedProjectId(data[0].id)
    }
  }

  const loadTasks = async () => {
    const params = new URLSearchParams()
    if (filterStatus !== 'ALL') params.set('status', filterStatus)
    if (filterPriority !== 'ALL') params.set('priority', filterPriority)
    if (filterAssignee) params.set('assigneeId', filterAssignee)
    if (taskQuery) params.set('q', taskQuery)
    params.set('page', '0')
    params.set('size', '50')
    params.set('sort', 'createdAt,desc')
    const data = await apiRequest(`/workspaces/${workspaceId}/tasks?${params.toString()}`)
    setTasks(data.content || [])
  }

  useEffect(() => {
    loadWorkspace().catch((err) => setError(err.message))
    loadProjects().catch((err) => setError(err.message))
    loadTasks().catch((err) => setError(err.message))
  }, [workspaceId, filterStatus, filterPriority, filterAssignee, taskQuery])

  const handleCreateProject = async () => {
    if (!projectName.trim()) return
    try {
      const data = await apiRequest(`/workspaces/${workspaceId}/projects`, {
        method: 'POST',
        body: JSON.stringify({
          name: projectName.trim(),
          description: projectDescription.trim(),
        }),
      })
      setProjectName('')
      setProjectDescription('')
      await loadProjects()
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
        }),
      })
      setTaskTitle('')
      setTaskDescription('')
      await loadTasks()
    } catch (err) {
      setError(err.message)
    }
  }

  const handleInvite = async () => {
    if (!inviteEmail.trim()) return
    try {
      const data = await apiRequest(`/workspaces/${workspaceId}/invites`, {
        method: 'POST',
        body: JSON.stringify({ email: inviteEmail.trim(), role: inviteRole }),
      })
      setInviteToken(data.token)
      setInviteEmail('')
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h2 className="panel-title">{workspace?.name || 'Workspace'}</h2>
          <p className="muted">Manage projects, tasks, and members.</p>
        </div>
        <Link className="button secondary" to="/workspaces">
          Back to workspaces
        </Link>
      </div>
      {error && <div className="alert">{error}</div>}
      <div className="layout">
        <div className="panel">
          <div className="panel-header">
            <h3 className="panel-title">Projects</h3>
          </div>
          <div className="list">
            {projects.length === 0 && <div className="empty">No projects yet.</div>}
            {projects.map((project) => (
              <Link
                key={project.id}
                to={`/workspaces/${workspaceId}/projects/${project.id}`}
                className={`list-item ${project.id === selectedProjectId ? 'active' : ''}`}
                onClick={() => setSelectedProjectId(project.id)}
              >
                <strong>{project.name}</strong>
                {project.description && <span className="muted">{project.description}</span>}
              </Link>
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
            <h3 className="panel-title">Tasks</h3>
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
              <Link key={task.id} to={`/tasks/${task.id}`} className="task-card">
                <strong>{task.title}</strong>
                <div className="muted">{task.description || 'No description'}</div>
                <div className="split">
                  <span className="badge">{task.status}</span>
                  <span className="badge">{task.priority}</span>
                </div>
              </Link>
            ))}
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
        </div>
      </div>
    </div>
  )
}
