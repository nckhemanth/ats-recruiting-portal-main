import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react'
import { api, setAuthToken } from '../api/client'
import type { Role, User } from '../api/types'

const TOKEN_KEY = 'rf_token'
interface Registration { email: string; password: string; fullName: string; role: Role; phone?: string; location?: string }
interface AuthValue {
  user: User | null
  loading: boolean
  login(email: string, password: string): Promise<User>
  register(request: Registration): Promise<User>
  logout(): void
  refresh(): Promise<void>
}

const AuthContext = createContext<AuthValue | null>(null)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null)
  const [loading, setLoading] = useState(true)

  async function refresh() {
    const token = localStorage.getItem(TOKEN_KEY)
    if (!token) { setUser(null); setLoading(false); return }
    setAuthToken(token)
    try { setUser((await api.get<User>('/auth/me')).data) }
    catch { localStorage.removeItem(TOKEN_KEY); setAuthToken(null); setUser(null) }
    finally { setLoading(false) }
  }

  useEffect(() => { void refresh() }, [])

  async function startSession(path: string, payload: object) {
    setLoading(true)
    try {
      const { data } = await api.post<{ token: string; user: User }>(path, payload)
      localStorage.setItem(TOKEN_KEY, data.token)
      setAuthToken(data.token)
      setUser(data.user)
      return data.user
    } finally { setLoading(false) }
  }

  const value = useMemo<AuthValue>(() => ({
    user, loading,
    login: (email, password) => startSession('/auth/login', { email, password }),
    register: request => startSession('/auth/register', request),
    logout: () => { localStorage.removeItem(TOKEN_KEY); setAuthToken(null); setUser(null) },
    refresh
  }), [user, loading])

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) throw new Error('useAuth must be used inside AuthProvider')
  return context
}

