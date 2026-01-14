import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { useAuth } from '../state/AuthContext.jsx'

export default function TaskPage() {
  const { taskId } = useParams()
  const { apiRequest } = useAuth()
  const [task, setTask] = useState(null)
  const [comments, setComments] = useState([])
  const [commentBody, setCommentBody] = useState('')
  const [status, setStatus] = useState('TODO')
  const [priority, setPriority] = useState('MED')
  const [error, setError] = useState('')

  const loadTask = async () => {
    const data = await apiRequest(`/tasks/${taskId}`)
    setTask(data)
    setStatus(data.status)
    setPriority(data.priority)
  }

  const loadComments = async () => {
    const data = await apiRequest(`/tasks/${taskId}/comments`)
    setComments(data)
  }

  useEffect(() => {
    loadTask().catch((err) => setError(err.message))
    loadComments().catch((err) => setError(err.message))
  }, [taskId])

  const handleUpdate = async () => {
    try {
      await apiRequest(`/tasks/${taskId}`, {
        method: 'PATCH',
        body: JSON.stringify({
          status,
          priority,
        }),
      })
      await loadTask()
    } catch (err) {
      setError(err.message)
    }
  }

  const handleComment = async () => {
    if (!commentBody.trim()) return
    try {
      await apiRequest(`/tasks/${taskId}/comments`, {
        method: 'POST',
        body: JSON.stringify({ body: commentBody.trim() }),
      })
      setCommentBody('')
      await loadComments()
    } catch (err) {
      setError(err.message)
    }
  }

  return (
    <div className="page">
      <div className="page-header">
        <div>
          <h2 className="panel-title">{task?.title || 'Task'}</h2>
          <p className="muted">{task?.description || 'Task detail and comments.'}</p>
        </div>
        <Link
          className="button secondary"
          to={
            task
              ? `/workspaces/${task.workspaceId}/projects/${task.projectId}`
              : '/workspaces'
          }
        >
          Back to project
        </Link>
      </div>
      {error && <div className="alert">{error}</div>}
      <div className="layout">
        <div className="panel">
          <div className="panel-header">
            <h3 className="panel-title">Status</h3>
          </div>
          <div className="form">
            <select className="select" value={status} onChange={(event) => setStatus(event.target.value)}>
              <option value="TODO">TODO</option>
              <option value="IN_PROGRESS">IN_PROGRESS</option>
              <option value="DONE">DONE</option>
            </select>
            <select className="select" value={priority} onChange={(event) => setPriority(event.target.value)}>
              <option value="LOW">LOW</option>
              <option value="MED">MED</option>
              <option value="HIGH">HIGH</option>
            </select>
            <button className="button" type="button" onClick={handleUpdate}>
              Update task
            </button>
          </div>
        </div>
        <div className="panel">
          <div className="panel-header">
            <h3 className="panel-title">Comments</h3>
          </div>
          <div className="form">
            <textarea
              className="textarea"
              value={commentBody}
              onChange={(event) => setCommentBody(event.target.value)}
              placeholder="Write a comment"
            />
            <button className="button secondary" type="button" onClick={handleComment}>
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
      </div>
    </div>
  )
}
