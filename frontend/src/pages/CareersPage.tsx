import { useEffect, useState, type FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { api, errorMessage } from '../api/client'
import type { Job, Page } from '../api/types'

export function CareersPage() {
  const [jobs, setJobs] = useState<Job[]>([])
  const [query, setQuery] = useState('')
  const [location, setLocation] = useState('')
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  async function load() {
    setLoading(true); setError('')
    try { setJobs((await api.get<Page<Job>>('/jobs', { params: { query, location, size: 12 } })).data.content) }
    catch (cause) { setError(errorMessage(cause)) }
    finally { setLoading(false) }
  }
  useEffect(() => { void load() }, [])
  const submit = (event: FormEvent) => { event.preventDefault(); void load() }

  return <>
    <section className="hero"><div className="container hero-grid">
      <div><p className="eyebrow">Careers at growing teams</p><h1>Find work where your next chapter can begin.</h1>
        <p className="lead">Explore thoughtfully designed roles, learn what each team values, and follow every application with transparent updates.</p></div>
      <div className="hero-panel"><strong>{jobs.length}</strong><span>open opportunities available now</span><p className="small">One candidate profile. Clear pipelines. Human communication.</p></div>
    </div></section>
    <section className="overlap"><div className="container panel">
      <form className="search-grid" onSubmit={submit}>
        <input className="input" value={query} onChange={e => setQuery(e.target.value)} placeholder="Search title, company, or keyword" />
        <input className="input" value={location} onChange={e => setLocation(e.target.value)} placeholder="Location" />
        <button className="button" type="submit">Search roles</button>
      </form>
    </div></section>
    <section className="section"><div className="container">
      <div className="section-title"><div><p className="eyebrow muted">Open positions</p><h2>Build something meaningful</h2></div><Link className="button secondary" to="/candidate/register">Create candidate profile</Link></div>
      {error && <p className="error">{error}</p>}
      {loading ? <div className="page-state">Loading opportunities…</div> : jobs.length === 0 ? <div className="empty">No roles match your search yet.</div> :
        <div className="grid job-grid">{jobs.map(job => <Link className="card lift" to={`/jobs/${job.id}`} key={job.id}>
          <span className="pill">{job.employmentType}</span><h3 style={{marginTop:14}}>{job.title}</h3>
          <div className="card-meta"><span>{job.company}</span><span>·</span><span>{job.location}</span></div>
          <p className="muted small">{job.department || 'General'} · Posted {new Date(job.createdAt).toLocaleDateString()}</p>
          <span className="small" style={{color:'#4f46e5',fontWeight:700}}>View role →</span>
        </Link>)}</div>}
    </div></section>
  </>
}

