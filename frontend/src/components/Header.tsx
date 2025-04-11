import { Link, NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../auth/AuthContext'

export function Header() {
  const { user, logout } = useAuth()
  const navigate = useNavigate()
  const signOut = () => { logout(); navigate('/') }
  return <header className="site-header">
    <div className="header-inner">
      <Link to="/" className="brand"><span className="brand-mark">RF</span><span>Recruit Flow</span></Link>
      <nav>
        <NavLink to="/">Careers</NavLink>
        <NavLink to="/candidate/login">Candidate Portal</NavLink>
        <NavLink to="/recruiter/login">Recruiter Console</NavLink>
      </nav>
      <div className="header-actions">
        <Link className="button compact" to={user ? (user.role === 'RECRUITER' ? '/recruiter/jobs' : '/candidate/dashboard') : '/candidate/register'}>
          {user ? 'Go to dashboard' : 'Join us'}
        </Link>
        {user && <button className="link-button" onClick={signOut}>Sign out</button>}
      </div>
    </div>
  </header>
}

