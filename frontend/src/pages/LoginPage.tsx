import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { errorMessage } from '../api/client'
import type { Role } from '../api/types'

export function LoginPage({ role }: { role: Role }) {
  const { login, loading } = useAuth()
  const navigate = useNavigate()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const candidate = role === 'CANDIDATE'
  async function submit(event: FormEvent) {
    event.preventDefault(); setError('')
    try { const user = await login(email, password); if (user.role !== role) throw new Error('Account does not have access to this portal'); navigate(candidate ? '/candidate/dashboard' : '/recruiter/jobs') }
    catch (cause) { setError(errorMessage(cause)) }
  }
  return <main className="auth-shell"><div className="auth-grid">
    <div className="auth-card"><h1>{candidate ? 'Candidate sign in' : 'Recruiter sign in'}</h1><p className="muted small">{candidate ? 'Track your applications and manage your profile.' : 'Manage job postings and review applicants.'}</p>
      <form className="form" onSubmit={submit}><div className="field"><label>Email</label><input type="email" required value={email} onChange={e => setEmail(e.target.value)} /></div>
        <div className="field"><label>Password</label><input type="password" required value={password} onChange={e => setPassword(e.target.value)} /></div>
        <button className="button" disabled={loading}>{loading ? 'Signing in…' : 'Sign in'}</button>{error && <p className="error">{error}</p>}</form>
      <p className="muted small">Need an account? <Link style={{color:'#4f46e5'}} to={candidate ? '/candidate/register' : '/recruiter/register'}>Create one</Link></p>
    </div>
    <div className="info-card"><h2>A better way to hire and get hired</h2><div className="info-grid"><div className="card"><h3>For candidates</h3><p className="muted small">Follow applications in real time, reuse your profile, and receive thoughtful updates.</p></div><div className="card"><h3>For recruiters</h3><p className="muted small">Launch roles in minutes, manage pipelines, and collaborate with hiring managers.</p></div></div></div>
  </div></main>
}

