import axios from 'axios';
import { API_BASE_URL } from '../constants';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' }
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) config.headers.Authorization = `Bearer ${token}`;
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// ==================== AUTH ====================
export const authAPI = {
  recruiterRegister: (data) => api.post('/auth/recruiter/register', data),
  recruiterLogin: (data) => api.post('/auth/recruiter/login', data),
  candidateRegister: (data) => api.post('/auth/candidate/register', data),
  candidateLogin: (data) => api.post('/auth/candidate/login', data)
};

// ==================== JOBS ====================
export const jobAPI = {
  browseJobs: (params) => api.get('/public/jobs', { params }),
  getPublicJobById: (id) => api.get(`/public/jobs/${id}`),
  createJob: (data) => api.post('/recruiter/jobs', data),
  getRecruiterJobs: () => api.get('/recruiter/jobs'),
  getJobById: (id) => api.get(`/recruiter/jobs/${id}`),
  updateJob: (id, data) => api.put(`/recruiter/jobs/${id}`, data),
  closeJob: (id) => api.patch(`/recruiter/jobs/${id}/close`),
  deleteJob: (id) => api.delete(`/recruiter/jobs/${id}`)
};

// ==================== APPLICATIONS ====================
export const applicationAPI = {
  apply: (data) => api.post('/applications', data),
  getCandidateApplications: () => api.get('/applications/candidate'),
  getJobApplications: (jobId) => api.get(`/applications/job/${jobId}`),
  getAllRecruiterApplications: () => api.get('/applications/recruiter'),
  updateStatus: (id, status, notes = '') => 
    api.patch(`/applications/${id}/status`, { status, notes })  // Now uses body, not params
};

// ==================== RESUME ====================
export const resumeAPI = {
  upload: (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post('/candidate/resume', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  }
};

// ==================== INTERVIEWS ====================
export const interviewAPI = {
  scheduleInterview: (data) => api.post('/interviews', data),
  getRecruiterInterviews: () => api.get('/interviews/recruiter'),
  getCandidateInterviews: () => api.get('/interviews/candidate'),
  cancelInterview: (id) => api.patch(`/interviews/${id}/cancel`)
};

// ==================== DASHBOARD ====================
export const dashboardAPI = {
  getRecruiterStats: () => api.get('/recruiter/dashboard/stats'),
  getCandidateStats: () => api.get('/candidate/dashboard/stats')
};

// ==================== PROFILE ====================
export const profileAPI = {
  getCandidateProfile: () => api.get('/candidate/profile'),
  updateCandidateProfile: (data) => api.put('/candidate/profile', data)
};

export default api;