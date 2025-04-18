import { useEffect, useState, type FormEvent } from 'react'
import { Link, useParams } from 'react-router-dom'
import { api, errorMessage } from '../api/client'
import type { Job } from '../api/types'
import { useAuth } from '../auth/AuthContext'

export function JobDetailPage() {
  const { id } = useParams()
  const { user } = useAuth()
  const [job, setJob] = useState<Job | null>(null)
  const [showApply, setShowApply] = useState(false)
  const [coverLetter, setCoverLetter] = useState('')
  const [resume, setResume] = useState<File | null>(null)
  const [message, setMessage] = useState('')
  useEffect(() => { api.get<Job>(`/jobs/${id}`).then(({data}) => setJob(data)).catch(cause => setMessage(errorMessage(cause))) }, [id])

  async function apply(event: FormEvent) {
    event.preventDefault(); setMessage('')
    const form = new FormData(); form.append('coverLetter', coverLetter); if (resume) form.append('resume', resume)
    try { await api.post(`/candidate/jobs/${id}/apply`, form); setMessage('Application submitted successfully.'); setShowApply(false) }
    catch (cause) { setMessage(errorMessage(cause)) }
  }
  if (!job) return <div className="container section"><div className="page-state">{message || 'Loading role…'}</div></div>
  return <>
    <section className="hero dark"><div className="container"><p className="eyebrow">{job.department || 'Open role'}</p><h1>{job.title}</h1><p className="lead">{job.company} · {job.location} · {job.employmentType}</p></div></section>
    <section className="section overlap"><div className="container detail-layout">
      <article className="panel"><h2>About the role</h2><p className="prose">{job.description}</p>{job.requirements && <><h2>What you’ll bring</h2><p className="prose">{job.requirements}</p></>}</article>
      <aside className="panel"><span className="pill">{job.status}</span><h3>Ready to apply?</h3><p className="muted small">Use your Recruit Flow profile and stay informed at every stage.</p>
        {user?.role === 'CANDIDATE' ? <button className="button" onClick={() => setShowApply(true)}>Apply for this role</button> : <Link className="button" to="/candidate/login">Sign in to apply</Link>}
        {message && <p className={message.includes('success') ? 'success' : 'error'}>{message}</p>}
      </aside>
    </div></section>
    {showApply && <div className="modal-backdrop"><div className="modal"><div className="toolbar"><h2>Apply to {job.title}</h2><button className="link-button" onClick={() => setShowApply(false)}>Close</button></div>
      <form className="form" onSubmit={apply}><div className="field"><label>Cover letter</label><textarea rows={7} value={coverLetter} onChange={e => setCoverLetter(e.target.value)} /></div>
        <div className="field"><label>Resume (PDF or Word, under 10 MB)</label><input type="file" accept=".pdf,.doc,.docx" onChange={e => setResume(e.target.files?.[0] ?? null)} /></div>
        <button className="button">Submit application</button></form></div></div>}
  </>
}

