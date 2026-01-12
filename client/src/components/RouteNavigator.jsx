import React, { useState } from 'react';
import { getNavigatorRoutes, addNavigatorRoute } from '../services/navigatorService';
import ErrorDisplay from './ErrorDisplay';
import './RouteNavigator.css';

const RouteNavigator = () => {
  const [fromId, setFromId] = useState('');
  const [toId, setToId] = useState('');
  const [orderBy, setOrderBy] = useState('name');
  const [distance, setDistance] = useState('');
  const [routes, setRoutes] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleGetRoutes = async (e) => {
    e.preventDefault();
    if (!fromId || !toId) {
      setError({ code: 400, message: 'Please enter both From ID and To ID' });
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await getNavigatorRoutes(fromId, toId, orderBy);
      setRoutes(response.data || []);
    } catch (err) {
      const errorData = err.response?.data || { code: 500, message: 'Failed to fetch navigator routes' };
      setError(errorData);
      setRoutes([]);
    } finally {
      setLoading(false);
    }
  };

  const handleAddRoute = async (e) => {
    e.preventDefault();
    if (!fromId || !toId || !distance) {
      setError({ code: 400, message: 'Please enter From ID, To ID, and Distance' });
      return;
    }

    setLoading(true);
    setError(null);

    try {
      await addNavigatorRoute(fromId, toId, parseInt(distance));
      setError(null);
      alert('Route added successfully!');
      setDistance('');
      // Refresh the list
      await handleGetRoutes({ preventDefault: () => {} });
    } catch (err) {
      const errorData = err.response?.data || { code: 500, message: 'Failed to add route' };
      setError(errorData);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="navigator-container">
      <h2>Route Navigator</h2>
      <ErrorDisplay error={error} />

      <div className="navigator-controls">
        <form onSubmit={handleGetRoutes} className="form-inline">
          <div className="form-group-inline">
            <label>From ID:</label>
            <input
              type="number"
              value={fromId}
              onChange={(e) => setFromId(e.target.value)}
              placeholder="Enter ID"
              min="1"
            />
          </div>

          <div className="form-group-inline">
            <label>To ID:</label>
            <input
              type="number"
              value={toId}
              onChange={(e) => setToId(e.target.value)}
              placeholder="Enter ID"
              min="1"
            />
          </div>

          <div className="form-group-inline">
            <label>Order By:</label>
            <select value={orderBy} onChange={(e) => setOrderBy(e.target.value)}>
              <option value="name">Name</option>
              <option value="distance">Distance</option>
              <option value="id">ID</option>
              <option value="creationDate">Creation Date</option>
            </select>
          </div>

          <button type="submit" disabled={loading}>
            {loading ? 'Loading...' : 'Get Routes'}
          </button>
        </form>

        <form onSubmit={handleAddRoute} className="form-inline">
          <div className="form-group-inline">
            <label>Distance:</label>
            <input
              type="number"
              value={distance}
              onChange={(e) => setDistance(e.target.value)}
              placeholder="Distance"
              min="2"
            />
          </div>

          <button type="submit" disabled={loading} className="btn-add">
            {loading ? 'Adding...' : 'Add Route'}
          </button>
        </form>
      </div>

      {routes.length > 0 && (
        <div className="navigator-results">
          <h3>Navigation Results ({routes.length} routes)</h3>
          <table className="navigator-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Distance</th>
                <th>From</th>
                <th>To</th>
                <th>Creation Date</th>
              </tr>
            </thead>
            <tbody>
              {routes.map(route => (
                <tr key={route.id}>
                  <td>{route.id}</td>
                  <td>{route.name}</td>
                  <td>{route.distance || 'N/A'}</td>
                  <td>{route.from?.name || 'N/A'}</td>
                  <td>{route.to?.name || 'N/A'}</td>
                  <td>{route.creationDate}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {routes.length === 0 && !error && !loading && (
        <div className="no-results">
          <p>No routes found. Use filters above to search.</p>
        </div>
      )}
    </div>
  );
};

export default RouteNavigator;
