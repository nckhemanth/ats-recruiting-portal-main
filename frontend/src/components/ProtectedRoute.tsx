import { Navigate, Outlet, useLocation } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import type { Role } from '../api/types'

export function ProtectedRoute({ role }: { role: Role }) {
  const { user, loading } = useAuth()
  const location = useLocation()
  if (loading) return <div className="page-state">Loading portal…</div>
  if (!user) return <Navigate to={role === 'RECRUITER' ? '/recruiter/login' : '/candidate/login'} state={{ from: location }} replace />
  if (user.role !== role) return <Navigate to={user.role === 'RECRUITER' ? '/recruiter/jobs' : '/candidate/dashboard'} replace />
  return <Outlet />
}

