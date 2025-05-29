import React, { createContext, useContext, useState, ReactNode } from 'react';
import { toast } from 'react-toastify';
import { startThreads, stopThreads, resetLogs } from '../api';

interface AppContextType {
  isSystemRunning: boolean;
  startSystem: () => Promise<void>;
  stopSystem: () => Promise<void>;
  resetSystem: () => Promise<void>;
  loading: boolean;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

export const AppProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [isSystemRunning, setIsSystemRunning] = useState(false);
  const [loading, setLoading] = useState(false);

  const startSystem = async () => {
    try {
      setLoading(true);
      await startThreads();
      setIsSystemRunning(true);
      toast.success('System started successfully');
    } catch (error) {
      toast.error('Failed to start system');
      console.error('Error starting system:', error);
    } finally {
      setLoading(false);
    }
  };

  const stopSystem = async () => {
    try {
      setLoading(true);
      await stopThreads();
      setIsSystemRunning(false);
      toast.success('System stopped successfully');
    } catch (error) {
      toast.error('Failed to stop system');
      console.error('Error stopping system:', error);
    } finally {
      setLoading(false);
    }
  };

  const resetSystem = async () => {
    try {
      setLoading(true);
      await resetLogs();
      toast.success('System logs reset successfully');
    } catch (error) {
      toast.error('Failed to reset system logs');
      console.error('Error resetting system logs:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <AppContext.Provider
      value={{
        isSystemRunning,
        startSystem,
        stopSystem,
        resetSystem,
        loading
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

export const useAppContext = (): AppContextType => {
  const context = useContext(AppContext);
  if (context === undefined) {
    throw new Error('useAppContext must be used within an AppProvider');
  }
  return context;
};