import { useState, type FormEvent } from 'react'
import { Link } from 'react-router-dom'
import { api, errorMessage } from '../api/client'
import { useAuth } from '../auth/AuthContext'

export function CandidateProfilePage() {
  const {user,refresh}=useAuth(); const [form,setForm]=useState({fullName:user?.fullName??'',phone:user?.phone??'',location:user?.location??'',bio:user?.bio??''}); const [message,setMessage]=useState('')
  const update=(key:string,value:string)=>setForm(current=>({...current,[key]:value}))
  async function submit(event:FormEvent){event.preventDefault();setMessage('');try{await api.put('/candidate/profile',form);await refresh();setMessage('Profile updated successfully.')}catch(cause){setMessage(errorMessage(cause))}}
  return <section className="section"><div className="container detail-layout"><div className="panel"><p className="eyebrow muted">Candidate profile</p><h1>Keep your story current</h1><form className="form" onSubmit={submit}><div className="field"><label>Full name</label><input required value={form.fullName} onChange={e=>update('fullName',e.target.value)}/></div><div className="form-row"><div className="field"><label>Phone</label><input value={form.phone} onChange={e=>update('phone',e.target.value)}/></div><div className="field"><label>Location</label><input value={form.location} onChange={e=>update('location',e.target.value)}/></div></div><div className="field"><label>About you</label><textarea rows={7} value={form.bio} onChange={e=>update('bio',e.target.value)}/></div><button className="button">Save profile</button>{message&&<p className={message.includes('success')?'success':'error'}>{message}</p>}</form></div>
    <aside className="panel"><h3>Your Recruit Flow profile</h3><p className="muted small">Used across every application so hiring teams always receive consistent, current information.</p><p className="small"><strong>Email</strong><br/>{user?.email}</p><Link className="button secondary" to="/candidate/dashboard">Back to dashboard</Link></aside></div></section>
}

