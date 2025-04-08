export type Role = 'CANDIDATE' | 'RECRUITER'

export interface User {
  id: number
  email: string
  fullName: string
  role: Role
  phone?: string
  location?: string
  bio?: string
  createdAt: string
}

export interface Stage { id: number; name: string; position: number }
export interface Job {
  id: number
  title: string
  company: string
  location: string
  department?: string
  employmentType: string
  status: string
  description?: string
  requirements?: string
  minSalary?: number
  maxSalary?: number
  createdAt: string
  applicationsCount: number
  stages?: Stage[]
}

export interface ApplicationNote { id: number; body: string; createdAt: string; authorName: string }
export interface Application {
  id: number
  status: string
  resumePath?: string
  coverLetter?: string
  createdAt: string
  updatedAt: string
  jobId: number
  jobTitle: string
  stage?: Stage
  candidate?: User
  notes: ApplicationNote[]
}

export interface Page<T> { content: T[]; totalElements: number; totalPages: number; number: number }

