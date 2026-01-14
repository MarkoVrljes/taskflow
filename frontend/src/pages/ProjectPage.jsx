import { useEffect, useMemo, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useAuth } from '../state/AuthContext.jsx'

export default function ProjectPage() {
  const { workspaceId, projectId } = useParams()
  const { apiRequest } = useAuth()
  const [project, setProject] = useState(null)
  const [tasks, setTasks] = useState([])
  const [taskTitle, setTaskTitle] = useState('')
  const [taskDescription, setTaskDescription] = useState('')
  const [taskPriority, setTaskPriority] = useState('MED')
  const [taskStatus, setTaskStatus] = useState('TODO')
  const [error, setError] = useState('')

  const visibleTasks = useMemo(() => tasks.filter((task) => task.projectId === projectId), [tasks, projectId])

  const loadProject = async () => {
    const data = await apiRequest(`/workspaces/${workspaceId}/projects`)
    const match = data.find((item) => item.id === projectId)
    setProject(match || null)
  }

  const loadTasks = async () => {
    const params = new URLSearchParams()
    params.set('page', '0')
    params.set('size', '50')
    params.set('sort', 'createdAt,desc')
    const data = await apiRequest(`/workspaces/${workspaceId}/tasks?${params.toString()}`)
    setTasks(data.content || [])
  }

  useEffect(() => {
    loadProject().catch((err) => setError(err.message))
    loadTasks().catch((err) => setError(err.message))
  }, [workspaceId, projectId])

  const handleCreateTask = async () => {
    if (!taskTitle.trim()) return
    try {
      await apiRequest(`/projects/${projectId}/tasks`, {
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

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h2 className="panel-title">{project?.name || 'Project'}</h2>
          <p className="muted">{project?.description || 'Project tasks and details.'}</p>
        </div>
        <Link className="button secondary" to={`/workspaces/${workspaceId}`}>
          Back to workspace
        </Link>
      </div>
      {error && <div className="alert">{error}</div>}
      <div className="layout">
        <div className="panel">
          <div className="panel-header">
            <h3 className="panel-title">Tasks</h3>
          </div>
          <div className="tasks">
            {visibleTasks.length === 0 && <div className="empty">No tasks yet.</div>}
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
        </div>
        <div className="panel">
          <div className="panel-header">
            <h3 className="panel-title">Add task</h3>
          </div>
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
