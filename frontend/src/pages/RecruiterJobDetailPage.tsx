import { useEffect, useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { api, errorMessage } from '../api/client'
import type { Application, Job, Stage } from '../api/types'

interface Detail { job: Job; applications: Application[] }

export function RecruiterJobDetailPage() {
  const {id}=useParams(); const[detail,setDetail]=useState<Detail|null>(null); const[error,setError]=useState(''); const[drafts,setDrafts]=useState<Record<number,string>>({})
  async function load(){try{setDetail((await api.get<Detail>(`/recruiter/jobs/${id}`)).data)}catch(cause){setError(errorMessage(cause))}}
  useEffect(()=>{void load()},[id])
  async function move(applicationId:number,stageId:number){try{await api.post(`/recruiter/applications/${applicationId}/move`,{stageId});await load()}catch(cause){setError(errorMessage(cause))}}
  async function note(applicationId:number){const body=drafts[applicationId]?.trim();if(!body)return;try{await api.post(`/recruiter/applications/${applicationId}/notes`,{body});setDrafts(current=>({...current,[applicationId]:''}));await load()}catch(cause){setError(errorMessage(cause))}}
  if(!detail)return <div className="container section"><div className="page-state">{error||'Loading pipeline…'}</div></div>
  const stages=detail.job.stages??[]
  return <><section className="hero dark"><div className="container dashboard-title"><div><p className="eyebrow">Recruiter console</p><h1>{detail.job.title}</h1><p className="lead">{detail.job.company} · {detail.job.location} · {detail.job.employmentType}</p></div><Link className="button secondary" to="/recruiter/jobs">← Back to job list</Link></div></section>
    <section className="overlap section"><div className="container"><article className="panel"><h2>Role overview</h2><p className="muted small">Applicants: {detail.applications.length}</p><p className="prose">{detail.job.description}</p>{detail.job.requirements&&<div className="card"><h3>Requirements</h3><p className="prose">{detail.job.requirements}</p></div>}</article>
      <section className="panel" style={{marginTop:24}}><div className="section-title"><div><h2>Pipeline</h2><p className="muted small">Move candidates between stages and keep interviewers aligned with internal notes.</p></div></div>{error&&<p className="error">{error}</p>}
        <div className="pipeline">{stages.map(stage=><StageColumn key={stage.id} stage={stage} applications={detail.applications.filter(item=>item.stage?.id===stage.id)} stages={stages} drafts={drafts} setDrafts={setDrafts} move={move} note={note}/>)}</div>
      </section></div></section></>
}

function StageColumn({stage,applications,stages,drafts,setDrafts,move,note}:{stage:Stage;applications:Application[];stages:Stage[];drafts:Record<number,string>;setDrafts:React.Dispatch<React.SetStateAction<Record<number,string>>>;move:(id:number,stage:number)=>Promise<void>;note:(id:number)=>Promise<void>}){
  return <div className="stage"><div className="stage-header"><strong>{stage.name}</strong><span className="small muted">{applications.length} candidates</span></div>{applications.length===0?<div className="empty small">No candidates in this stage.</div>:applications.map(item=><article className="candidate-card" key={item.id}><div className="toolbar"><div><strong>{item.candidate?.fullName||item.candidate?.email}</strong><p className="muted small">Applied {new Date(item.createdAt).toLocaleDateString()}</p></div>{item.resumePath&&<a className="small" style={{color:'#4f46e5',fontWeight:700}} href={`${import.meta.env.VITE_API_URL?.replace('/api','')??'http://localhost:8080'}${item.resumePath}`} target="_blank">Resume</a>}</div>
    <div className="field"><label>Move to stage</label><select value={item.stage?.id} onChange={e=>void move(item.id,Number(e.target.value))}>{stages.map(option=><option value={option.id} key={option.id}>{option.name}</option>)}</select></div>
    {item.notes.map(entry=><div className="note" key={entry.id}>{entry.body}<br/><span className="muted">{entry.authorName} · {new Date(entry.createdAt).toLocaleString()}</span></div>)}
    <div className="field" style={{marginTop:10}}><textarea rows={2} placeholder="Add internal note" value={drafts[item.id]??''} onChange={e=>setDrafts(current=>({...current,[item.id]:e.target.value}))}/><button className="button compact" onClick={()=>void note(item.id)}>Add note</button></div>
  </article>)}</div>
}

