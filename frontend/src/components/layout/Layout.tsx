import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { Menu, X, LayoutDashboard, Users, Ticket, Settings, UserCog, LogOut, Activity } from 'lucide-react';
import { Button } from '../ui/Button';
import { useAppContext } from '../../context/AppContext';

interface NavItemProps {
  to: string;
  icon: React.ReactNode;
  label: string;
  isActive: boolean;
}

const NavItem: React.FC<NavItemProps> = ({ to, icon, label, isActive }) => {
  return (
    <Link
      to={to}
      className={`flex items-center px-4 py-3 text-sm font-medium rounded-md transition-colors ${
        isActive
          ? 'bg-blue-800 text-white'
          : 'text-gray-300 hover:bg-blue-700 hover:text-white'
      }`}
    >
      <span className="mr-3 h-5 w-5">{icon}</span>
      {label}
    </Link>
  );
};

export const Layout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [sidebarOpen, setSidebarOpen] = React.useState(false);
  const location = useLocation();
  const { isSystemRunning, startSystem, stopSystem, resetSystem, loading } = useAppContext();

  const navItems = [
    { to: '/', icon: <LayoutDashboard size={20} />, label: 'Tableau de bord' },
    { to: '/vendors', icon: <UserCog size={20} />, label: 'Vendeurs' },
    { to: '/customers', icon: <Users size={20} />, label: 'Clients' },
    { to: '/tickets', icon: <Ticket size={20} />, label: 'Billets' },
    { to: '/settings', icon: <Settings size={20} />, label: 'Paramètres' },
  ];

  return (
    <div className="min-h-screen bg-gray-100">
      {/* Mobile Sidebar Toggle */}
      <div className="fixed inset-0 flex z-40 lg:hidden" role="dialog" aria-modal="true">
        <div
          className={`fixed inset-0 bg-gray-600 bg-opacity-75 transition-opacity ${
            sidebarOpen ? 'opacity-100' : 'opacity-0 pointer-events-none'
          }`}
          aria-hidden="true"
          onClick={() => setSidebarOpen(false)}
        ></div>

        <div
          className={`relative flex-1 flex flex-col max-w-xs w-full pt-5 pb-4 bg-blue-900 transition transform ${
            sidebarOpen ? 'translate-x-0' : '-translate-x-full'
          }`}
        >
          <div className="absolute top-0 right-0 -mr-12 pt-2">
            <button
              type="button"
              className="ml-1 flex items-center justify-center h-10 w-10 rounded-full focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
              onClick={() => setSidebarOpen(false)}
            >
              <span className="sr-only">Fermer la barre latérale</span>
              <X className="h-6 w-6 text-white" aria-hidden="true" />
            </button>
          </div>

          <div className="flex-shrink-0 flex items-center px-4">
            <h1 className="text-xl font-bold text-white">Système de billetterie</h1>
          </div>
          <div className="mt-5 flex-1 h-0 overflow-y-auto">
            <nav className="px-2 space-y-1">
              {navItems.map((item) => (
                <NavItem
                  key={item.to}
                  to={item.to}
                  icon={item.icon}
                  label={item.label}
                  isActive={location.pathname === item.to}
                />
              ))}
            </nav>
          </div>
          <div className="px-4 py-4 border-t border-blue-800">
            <div className="flex flex-col space-y-2">
              <Button
                variant={isSystemRunning ? 'danger' : 'success'}
                onClick={isSystemRunning ? stopSystem : startSystem}
                isLoading={loading}
                className="w-full"
              >
                <Activity className="mr-2 h-4 w-4" />
                {isSystemRunning ? 'Arrêter le système' : 'Démarrer le système'}
              </Button>
              <Button
                variant="outline"
                onClick={resetSystem}
                isLoading={loading}
                className="w-full bg-blue-800 text-white border-blue-700 hover:bg-blue-700"
              >
                Réinitialiser les journaux
              </Button>
            </div>
          </div>
        </div>
      </div>

      {/* Static sidebar for desktop */}
      <div className="hidden lg:flex lg:w-64 lg:flex-col lg:fixed lg:inset-y-0">
        <div className="flex-1 flex flex-col min-h-0 bg-blue-900">
          <div className="flex-1 flex flex-col pt-5 pb-4 overflow-y-auto">
            <div className="flex items-center flex-shrink-0 px-4">
              <h1 className="text-xl font-bold text-white">Système de billetterie</h1>
            </div>
            <nav className="mt-5 flex-1 px-2 space-y-1">
              {navItems.map((item) => (
                <NavItem
                  key={item.to}
                  to={item.to}
                  icon={item.icon}
                  label={item.label}
                  isActive={location.pathname === item.to}
                />
              ))}
            </nav>
          </div>
          <div className="px-4 py-4 border-t border-blue-800">
            <div className="flex flex-col space-y-2">
              <Button
                variant={isSystemRunning ? 'danger' : 'success'}
                onClick={isSystemRunning ? stopSystem : startSystem}
                isLoading={loading}
                className="w-full"
              >
                <Activity className="mr-2 h-4 w-4" />
                {isSystemRunning ? 'Arrêter le système' : 'Démarrer le système'}
              </Button>
              <Button
                variant="outline"
                onClick={resetSystem}
                isLoading={loading}
                className="w-full bg-blue-800 text-white border-blue-700 hover:bg-blue-700"
              >
                Réinitialiser les journaux
              </Button>
            </div>
          </div>
        </div>
      </div>

      {/* Main content */}
      <div className="lg:pl-64 flex flex-col">
        <div className="sticky top-0 z-10 flex-shrink-0 flex h-16 bg-white shadow">
          <button
            type="button"
            className="px-4 border-r border-gray-200 text-gray-500 lg:hidden"
            onClick={() => setSidebarOpen(true)}
          >
            <span className="sr-only">Ouvrir la barre latérale</span>
            <Menu className="h-6 w-6" aria-hidden="true" />
          </button>
          <div className="flex-1 px-4 flex justify-between">
            <div className="flex-1 flex items-center">
              <h1 className="text-lg font-semibold text-gray-800">
                {navItems.find(item => item.to === location.pathname)?.label || 'Tableau de bord'}
              </h1>
            </div>
            <div className="ml-4 flex items-center md:ml-6">
              <div className="flex items-center gap-2">
                <div className={`h-3 w-3 rounded-full ${isSystemRunning ? 'bg-green-500' : 'bg-red-500'}`}></div>
                <span className="text-sm text-gray-600">
                  {isSystemRunning ? 'Système en cours d’exécution' : 'Système arrêté'}
                </span>
              </div>
            </div>
          </div>
        </div>

        <main className="flex-1">
          <div className="py-6">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 md:px-8">{children}</div>
          </div>
        </main>
      </div>
    </div>
  );
};