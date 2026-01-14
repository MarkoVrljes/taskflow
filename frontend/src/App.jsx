import { Navigate, Route, Routes } from 'react-router-dom'
import './App.css'
import AppLayout from './components/AppLayout.jsx'
import LoginPage from './pages/LoginPage.jsx'
import WorkspacesPage from './pages/WorkspacesPage.jsx'
import WorkspacePage from './pages/WorkspacePage.jsx'
import ProjectPage from './pages/ProjectPage.jsx'
import TaskPage from './pages/TaskPage.jsx'
import { useAuth } from './state/AuthContext.jsx'

function RequireAuth({ children }) {
  const { isAuthed } = useAuth()
  if (!isAuthed) {
    return <Navigate to="/login" replace />
  }
  return children
}

function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        element={
          <RequireAuth>
            <AppLayout />
          </RequireAuth>
        }
      >
        <Route index element={<Navigate to="/workspaces" replace />} />
        <Route path="/workspaces" element={<WorkspacesPage />} />
        <Route path="/workspaces/:workspaceId" element={<WorkspacePage />} />
        <Route path="/workspaces/:workspaceId/projects/:projectId" element={<ProjectPage />} />
        <Route path="/tasks/:taskId" element={<TaskPage />} />
      </Route>
      <Route path="*" element={<Navigate to="/workspaces" replace />} />
    </Routes>
  )
}

export default App
