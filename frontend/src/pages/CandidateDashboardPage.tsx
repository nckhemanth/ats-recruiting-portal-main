import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api, errorMessage } from '../api/client'
import type { Application } from '../api/types'
import { useAuth } from '../auth/AuthContext'

export function CandidateDashboardPage() {
  const { user } = useAuth(); const [items,setItems]=useState<Application[]>([]); const [error,setError]=useState(''); const [loading,setLoading]=useState(true)
  useEffect(()=>{api.get<Application[]>('/candidate/applications').then(({data})=>setItems(data)).catch(cause=>setError(errorMessage(cause))).finally(()=>setLoading(false))},[])
  return <><section className="dashboard-hero"><div className="container dashboard-title"><div><p className="eyebrow">Candidate dashboard</p><h1>Welcome back, {user?.fullName}</h1><p>Track every application, update your profile, and discover new roles tailored to you.</p></div><div><Link className="button secondary" to="/candidate/profile">Edit profile</Link></div></div></section>
    <section className="overlap section"><div className="container panel"><div className="section-title"><div><h2>Your applications</h2><p className="muted small">A clear view of every conversation in progress.</p></div><Link className="button" to="/">Explore roles</Link></div>
      {error&&<p className="error">{error}</p>}{loading?<div className="page-state">Loading applications…</div>:items.length===0?<div className="empty">You have not applied yet. Explore open roles and start your next chapter.</div>:<div className="list">{items.map(item=><div className="list-item" key={item.id}><div><h3>{item.jobTitle}</h3><p className="muted small">Applied {new Date(item.createdAt).toLocaleDateString()} · Updated {new Date(item.updatedAt).toLocaleDateString()}</p></div><div style={{display:'flex',gap:12,alignItems:'center'}}><span className="status">{item.stage?.name??item.status}</span><Link className="button compact secondary" to={`/jobs/${item.jobId}`}>View role</Link></div></div>)}</div>}
    </div></section></>
}

