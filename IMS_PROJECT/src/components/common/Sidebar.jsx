import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { 
  LayoutDashboard, Briefcase, Users, Calendar,
  FileText, User, BookOpen, LogOut
} from 'lucide-react';
import '../../styles/Sidebar.css';

const Sidebar = ({ role }) => {
  const { logout } = useAuth();

  const recruiterLinks = [
    { to: '/recruiter/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
    { to: '/recruiter/jobs', icon: Briefcase, label: 'Jobs' },
    { to: '/recruiter/applicants', icon: Users, label: 'Applicants' },
    { to: '/recruiter/interviews', icon: Calendar, label: 'Interviews' }
  ];

  const candidateLinks = [
    { to: '/candidate/dashboard', icon: LayoutDashboard, label: 'Dashboard' },
    { to: '/candidate/jobs', icon: Briefcase, label: 'Browse Jobs' },
    { to: '/candidate/applications', icon: FileText, label: 'My Applications' },
    { to: '/candidate/interviews', icon: Calendar, label: 'Interviews' },
    { to: '/candidate/profile', icon: User, label: 'Profile' }
  ];

  const links = role === 'RECRUITER' ? recruiterLinks : candidateLinks;

  const handleLogout = () => {
    logout();
    window.location.href = '/';
  };

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <Briefcase size={28} className="sidebar-logo" />
        <h2>Interview Portal</h2>
      </div>
      <nav className="sidebar-nav">
        {links.map((link) => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) => 
              `sidebar-link ${isActive ? 'active' : ''}`
            }
          >
            <link.icon size={20} />
            <span>{link.label}</span>
          </NavLink>
        ))}
      </nav>
      <div className="sidebar-footer">
        <button onClick={handleLogout} className="sidebar-logout">
          <LogOut size={20} />
          <span>Logout</span>
        </button>
      </div>
    </aside>
  );
};

export default Sidebar;