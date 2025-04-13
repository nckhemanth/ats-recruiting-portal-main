import { Route, Routes } from 'react-router-dom'
import { Header } from './components/Header'
import { ProtectedRoute } from './components/ProtectedRoute'
import { CareersPage } from './pages/CareersPage'
import { JobDetailPage } from './pages/JobDetailPage'
import { LoginPage } from './pages/LoginPage'
import { RegisterPage } from './pages/RegisterPage'
import { CandidateDashboardPage } from './pages/CandidateDashboardPage'
import { CandidateProfilePage } from './pages/CandidateProfilePage'
import { RecruiterJobsPage } from './pages/RecruiterJobsPage'
import { RecruiterJobDetailPage } from './pages/RecruiterJobDetailPage'

export default function App() {
  return <div className="app"><Header /><Routes>
    <Route path="/" element={<CareersPage />} />
    <Route path="/jobs/:id" element={<JobDetailPage />} />
    <Route path="/candidate/login" element={<LoginPage role="CANDIDATE" />} />
    <Route path="/recruiter/login" element={<LoginPage role="RECRUITER" />} />
    <Route path="/candidate/register" element={<RegisterPage role="CANDIDATE" />} />
    <Route path="/recruiter/register" element={<RegisterPage role="RECRUITER" />} />
    <Route element={<ProtectedRoute role="CANDIDATE" />}>
      <Route path="/candidate/dashboard" element={<CandidateDashboardPage />} />
      <Route path="/candidate/profile" element={<CandidateProfilePage />} />
    </Route>
    <Route element={<ProtectedRoute role="RECRUITER" />}>
      <Route path="/recruiter/jobs" element={<RecruiterJobsPage />} />
      <Route path="/recruiter/jobs/:id" element={<RecruiterJobDetailPage />} />
    </Route>
  </Routes></div>
}

