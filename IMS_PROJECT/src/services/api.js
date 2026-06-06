import axios from 'axios';
import { API_BASE_URL } from '../constants';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: { 'Content-Type': 'application/json' }
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (res) => res,
  (err) => {
    if (err.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(err);
  }
);

// Auth
export const authAPI = {
  candidateLogin: (data) => api.post('/auth/candidate/login', data),
  candidateRegister: (data) => api.post('/auth/candidate/register', data),
  recruiterLogin: (data) => api.post('/auth/recruiter/login', data),
  recruiterRegister: (data) => api.post('/auth/recruiter/register', data)
};

// Jobs
export const jobAPI = {
  browseJobs: (params) => api.get('/public/jobs', { params }),
  getRecruiterJobs: () => api.get('/recruiter/jobs'),
  createJob: (data) => api.post('/recruiter/jobs', data),
  getJobById: (id) => api.get(`/recruiter/jobs/${id}`),
  updateJob: (id, data) => api.put(`/recruiter/jobs/${id}`, data),
  closeJob: (id) => api.patch(`/recruiter/jobs/${id}/close`),
  deleteJob: (id) => api.delete(`/recruiter/jobs/${id}`)
};

// Applications
export const applicationAPI = {
  apply: (jobId, coverLetter, resumeFile, guestName, guestEmail) => {
    const formData = new FormData();
    const request = { jobId, coverLetter };
    if (guestName) request.guestName = guestName;
    if (guestEmail) request.guestEmail = guestEmail;
    formData.append('request', new Blob([JSON.stringify(request)], { type: 'application/json' }));
    if (resumeFile) formData.append('resume', resumeFile);
    return api.post('/applications', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  },
  getCandidateApplications: () => api.get('/applications/candidate'),
  getAllRecruiterApplications: () => api.get('/applications/recruiter'),
  getJobApplications: (jobId) => api.get(`/applications/job/${jobId}`),
  updateStatus: (id, status, notes) => api.patch(`/applications/${id}/status`, { status, notes })
};

// Resume
export const resumeAPI = {
  upload: (file) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post('/candidate/resume', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
  }
};

// Interviews
export const interviewAPI = {
  scheduleInterview: (data) => api.post('/interviews', data),
  getRecruiterInterviews: () => api.get('/interviews/recruiter'),
  getCandidateInterviews: () => api.get('/interviews/candidate'),
  cancelInterview: (id) => api.patch(`/interviews/${id}/cancel`)
};

// Dashboard stats
export const dashboardAPI = {
  getCandidateStats: () => api.get('/candidate/dashboard/stats'),
  getRecruiterStats: () => api.get('/recruiter/dashboard/stats')
};

// Profile
export const profileAPI = {
  getCandidateProfile: () => api.get('/candidate/profile'),
  updateCandidateProfile: (data) => api.put('/candidate/profile', data)
};

// ATS
export const atsAPI = {
  scan: (jobId) => api.post('/ats/scan', { jobId }),
  scanApplication: (applicationId) => api.post(`/ats/scan-application/${applicationId}`)
};

export default api;