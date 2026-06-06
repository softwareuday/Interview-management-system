import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { LayoutDashboard, Briefcase, Users, Calendar, FileText, User, BookOpen, LogOut } from 'lucide-react';

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
    { to: '/candidate/applications', icon: FileText, label: 'Applications' },
    { to: '/candidate/interviews', icon: Calendar, label: 'Interviews' },
    { to: '/candidate/profile', icon: User, label: 'Profile' }
  ];

  const links = role === 'RECRUITER' ? recruiterLinks : candidateLinks;

  return (
    <aside className="sidebar">
      <div className="sidebar-header">InterviewPortal</div>
      <nav className="sidebar-nav">
        {links.map((link) => (
          <NavLink key={link.to} to={link.to} className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}>
            <link.icon size={20} />
            <span>{link.label}</span>
          </NavLink>
        ))}
      </nav>
      <button onClick={logout} className="sidebar-logout">
        <LogOut size={20} />
        <span>Logout</span>
      </button>
    </aside>
  );
};

export default Sidebar;