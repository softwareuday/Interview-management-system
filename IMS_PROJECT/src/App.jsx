import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import ProtectedRoute from './components/common/ProtectedRoute';

// Public Pages
import Landing from './pages/Landing';
import Login from './pages/Login';
import Register from './pages/Register';

// Recruiter Pages
import RecruiterDashboard from './pages/recruiter/RecruiterDashboard';
import Jobs from './pages/recruiter/Jobs';
import CreateJob from './pages/recruiter/CreateJob';
import EditJob from './pages/recruiter/EditJob';
import Applicants from './pages/recruiter/Applicants';
import Interviews from './pages/recruiter/Interviews';

// Candidate Pages
import CandidateDashboard from './pages/candidate/CandidateDashboard';
import BrowseJobs from './pages/candidate/BrowseJobs';
import Applications from './pages/candidate/Applications';
import CandidateInterviews from './pages/candidate/CandidateInterviews';
import Profile from './pages/candidate/Profile';

const HomeRedirect = () => {
  const { user, isAuthenticated } = useAuth();
  if (!isAuthenticated) return <Landing />;
  if (user?.role === 'RECRUITER') return <Navigate to="/recruiter/dashboard" replace />;
  if (user?.role === 'CANDIDATE') return <Navigate to="/candidate/dashboard" replace />;
  return <Landing />;
};

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomeRedirect />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          {/* Recruiter Routes */}
          <Route path="/recruiter/dashboard" element={<ProtectedRoute allowedRoles={['RECRUITER']}><RecruiterDashboard /></ProtectedRoute>} />
          <Route path="/recruiter/jobs" element={<ProtectedRoute allowedRoles={['RECRUITER']}><Jobs /></ProtectedRoute>} />
          <Route path="/recruiter/jobs/create" element={<ProtectedRoute allowedRoles={['RECRUITER']}><CreateJob /></ProtectedRoute>} />
          <Route path="/recruiter/jobs/:id/edit" element={<ProtectedRoute allowedRoles={['RECRUITER']}><EditJob /></ProtectedRoute>} />
          <Route path="/recruiter/applicants" element={<ProtectedRoute allowedRoles={['RECRUITER']}><Applicants /></ProtectedRoute>} />
          <Route path="/recruiter/interviews" element={<ProtectedRoute allowedRoles={['RECRUITER']}><Interviews /></ProtectedRoute>} />

          {/* Candidate Routes */}
          <Route path="/candidate/dashboard" element={<ProtectedRoute allowedRoles={['CANDIDATE']}><CandidateDashboard /></ProtectedRoute>} />
          <Route path="/candidate/jobs" element={<ProtectedRoute allowedRoles={['CANDIDATE']}><BrowseJobs /></ProtectedRoute>} />
          <Route path="/candidate/applications" element={<ProtectedRoute allowedRoles={['CANDIDATE']}><Applications /></ProtectedRoute>} />
          <Route path="/candidate/interviews" element={<ProtectedRoute allowedRoles={['CANDIDATE']}><CandidateInterviews /></ProtectedRoute>} />
          <Route path="/candidate/profile" element={<ProtectedRoute allowedRoles={['CANDIDATE']}><Profile /></ProtectedRoute>} />

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;