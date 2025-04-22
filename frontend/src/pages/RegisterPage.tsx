import { useState, type FormEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'
import { errorMessage } from '../api/client'
import type { Role } from '../api/types'

export function RegisterPage({ role }: { role: Role }) {
  const { register, loading } = useAuth(); const navigate = useNavigate()
  const [form, setForm] = useState({fullName:'',email:'',password:'',phone:'',location:''}); const [error,setError]=useState('')
  const candidate = role === 'CANDIDATE'
  const update = (key: string, value: string) => setForm(current => ({...current,[key]:value}))
  async function submit(event: FormEvent) { event.preventDefault(); setError(''); try { await register({...form,role}); navigate(candidate?'/candidate/dashboard':'/recruiter/jobs') } catch(cause){setError(errorMessage(cause))} }
  return <main className="auth-shell"><div className="auth-grid">
    <div className="auth-card"><h1>{candidate?'Create your candidate profile':'Set up recruiter access'}</h1><p className="muted small">{candidate?'Keep your details in one place and get transparent updates every step of the way.':'Create a team account to post jobs and review applicants.'}</p>
      <form className="form" onSubmit={submit}><div className="field"><label>Full name</label><input required value={form.fullName} onChange={e=>update('fullName',e.target.value)}/></div>
        <div className="field"><label>{candidate?'Email':'Work email'}</label><input type="email" required value={form.email} onChange={e=>update('email',e.target.value)}/></div>
        <div className="field"><label>Password</label><input type="password" minLength={8} required value={form.password} onChange={e=>update('password',e.target.value)}/></div>
        {candidate&&<div className="form-row"><div className="field"><label>Phone (optional)</label><input value={form.phone} onChange={e=>update('phone',e.target.value)}/></div><div className="field"><label>Location (optional)</label><input value={form.location} onChange={e=>update('location',e.target.value)}/></div></div>}
        <button className="button" disabled={loading}>{loading?'Creating account…':'Create account'}</button>{error&&<p className="error">{error}</p>}</form>
      <p className="muted small">Already registered? <Link style={{color:'#4f46e5'}} to={candidate?'/candidate/login':'/recruiter/login'}>Log in</Link></p>
    </div>
    <div className="info-card"><h2>{candidate?'What candidates gain':'Build a calmer hiring process'}</h2><div className="list"><div className="card"><h3>Transparent progress</h3><p className="muted small">See every stage and update without chasing email threads.</p></div><div className="card"><h3>One reusable profile</h3><p className="muted small">Keep contact details and resumes ready for future opportunities.</p></div><div className="card"><h3>Structured collaboration</h3><p className="muted small">Give hiring teams a shared pipeline and useful internal notes.</p></div></div></div>
  </div></main>
}

