import React, { useState } from 'react';
import RouteList from './components/RouteList';
import RouteForm from './components/RouteForm';
import RouteNavigator from './components/RouteNavigator';
import './App.css';

function App() {
  const [tab, setTab] = useState('list');
  const [selectedRoute, setSelectedRoute] = useState(null);
  const [refreshKey, setRefreshKey] = useState(0);

  const handleEdit = (route) => {
    setSelectedRoute(route);
    setTab('form');
  };

  const handleSave = () => {
    setSelectedRoute(null);
    setTab('list');
    setRefreshKey(prev => prev + 1);
  };

  const handleCancel = () => {
    setSelectedRoute(null);
    setTab('list');
  };

  const handleNewRoute = () => {
    setSelectedRoute(null);
    setTab('form');
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>Route Management System</h1>
        <p>Service 1 (Tomcat) & Service 2 (WildFly) Client</p>
      </header>

      <nav className="app-nav">
        <button
          className={`nav-btn ${tab === 'list' ? 'active' : ''}`}
          onClick={() => setTab('list')}
        >
          📋 Routes List
        </button>
        <button
          className={`nav-btn ${tab === 'form' ? 'active' : ''}`}
          onClick={handleNewRoute}
        >
          ➕ Create Route
        </button>
        <button
          className={`nav-btn ${tab === 'navigator' ? 'active' : ''}`}
          onClick={() => setTab('navigator')}
        >
          🧭 Navigator
        </button>
      </nav>

      <main className="app-content">
        {tab === 'list' && (
          <RouteList 
            refresh={refreshKey}
            onEdit={handleEdit}
          />
        )}

        {tab === 'form' && (
          <RouteForm
            route={selectedRoute}
            onSave={handleSave}
            onCancel={handleCancel}
          />
        )}

        {tab === 'navigator' && (
          <RouteNavigator />
        )}
      </main>

      <footer className="app-footer">
        <p>&copy; 2025 Route Management System. HTTPS Only.</p>
      </footer>
    </div>
  );
}

export default App;
