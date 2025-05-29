import React from 'react';

interface CardProps {
  children: React.ReactNode;
  title?: string;
  className?: string;
}

export const Card: React.FC<CardProps> = ({ children, title, className = '' }) => {
  return (
    <div className={`bg-white rounded-lg shadow-md overflow-hidden transition-all duration-300 hover:shadow-lg ${className}`}>
      {title && (
        <div className="border-b border-gray-200 px-6 py-4">
          <h3 className="text-lg font-semibold text-gray-800">{title}</h3>
        </div>
      )}
      <div className="p-6">{children}</div>
    </div>
  );
};

export const StatCard: React.FC<{
  title: string;
  value: string | number;
  icon: React.ReactNode;
  trend?: 'up' | 'down' | 'neutral';
  trendValue?: string;
  className?: string;
}> = ({ title, value, icon, trend, trendValue, className = '' }) => {
  const trendColor = 
    trend === 'up' ? 'text-green-500' : 
    trend === 'down' ? 'text-red-500' : 
    'text-gray-500';

  const trendIcon = 
    trend === 'up' ? '↑' : 
    trend === 'down' ? '↓' : 
    '→';

  return (
    <div className={`bg-white rounded-lg shadow-md p-6 transition-all duration-300 hover:shadow-lg ${className}`}>
      <div className="flex justify-between items-start">
        <div>
          <p className="text-sm font-medium text-gray-500">{title}</p>
          <p className="mt-2 text-3xl font-bold text-gray-900">{value}</p>
          {trend && trendValue && (
            <p className={`mt-2 text-sm ${trendColor} flex items-center`}>
              <span className="mr-1">{trendIcon}</span>
              {trendValue}
            </p>
          )}
        </div>
        <div className="p-2 bg-blue-50 rounded-md text-blue-700">
          {icon}
        </div>
      </div>
    </div>
  );
};