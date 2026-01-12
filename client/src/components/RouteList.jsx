import React, { useState, useEffect } from 'react';
import { getRoutes, deleteRoute } from '../services/apiService';
import ErrorDisplay from './ErrorDisplay';
import './RouteList.css';

const RouteList = ({ refresh, onEdit }) => {
  const [routes, setRoutes] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(5);
  const [sort, setSort] = useState('id');
  const [filters, setFilters] = useState({});

  useEffect(() => {
    fetchRoutes();
  }, [refresh, page, size, sort, filters]);

  const fetchRoutes = async () => {
    setLoading(true);
    setError(null);
    try {
      const params = {
        ...filters,
        page,
        size,
        sort,
      };
      const response = await getRoutes(params);
      setRoutes(response.data.data || []);
    } catch (err) {
      const errorData = err.response?.data || { code: 500, message: 'Failed to fetch routes' };
      setError(errorData);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this route?')) {
      try {
        await deleteRoute(id);
        fetchRoutes();
      } catch (err) {
        const errorData = err.response?.data || { code: 500, message: 'Failed to delete route' };
        setError(errorData);
      }
    }
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters(prev => ({ ...prev, [name]: value }));
    setPage(0);
  };

  const handleSortChange = (e) => {
    setSort(e.target.value);
  };

  const handleEdit = (route) => {
    onEdit(route);
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="route-list-container">
      <h2>Routes List</h2>
      <ErrorDisplay error={error} />

      <div className="filters-section">
        <h3>Filters & Sort</h3>
        <div className="filter-group">
          <label>Name:</label>
          <input
            type="text"
            name="namelike"
            placeholder="Filter by name"
            onChange={handleFilterChange}
          />
        </div>

        <div className="filter-group">
          <label>Distance:</label>
          <input
            type="number"
            name="distance"
            placeholder="Exact distance"
            onChange={handleFilterChange}
          />
        </div>

        <div className="filter-group">
          <label>Distance > than:</label>
          <input
            type="number"
            name="distancegt"
            placeholder="Greater than"
            onChange={handleFilterChange}
          />
        </div>

        <div className="filter-group">
          <label>Distance &lt; than:</label>
          <input
            type="number"
            name="distancelt"
            placeholder="Less than"
            onChange={handleFilterChange}
          />
        </div>

        <div className="filter-group">
          <label>From Name:</label>
          <input
            type="text"
            name="fromNamelike"
            placeholder="From location"
            onChange={handleFilterChange}
          />
        </div>

        <div className="filter-group">
          <label>To Name:</label>
          <input
            type="text"
            name="toNamelike"
            placeholder="To location"
            onChange={handleFilterChange}
          />
        </div>

        <div className="filter-group">
          <label>Sort by:</label>
          <select value={sort} onChange={handleSortChange}>
            <option value="id">ID</option>
            <option value="name">Name</option>
            <option value="distance">Distance</option>
            <option value="creationDate">Creation Date</option>
            <option value="-id">ID (Desc)</option>
            <option value="-name">Name (Desc)</option>
            <option value="-distance">Distance (Desc)</option>
            <option value="-creationDate">Creation Date (Desc)</option>
          </select>
        </div>

        <div className="filter-group">
          <label>Items per page:</label>
          <select value={size} onChange={(e) => { setSize(parseInt(e.target.value)); setPage(0); }}>
            <option value="5">5</option>
            <option value="10">10</option>
            <option value="20">20</option>
            <option value="50">50</option>
          </select>
        </div>
      </div>

      <table className="routes-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Distance</th>
            <th>Creation Date</th>
            <th>From</th>
            <th>To</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {routes.length === 0 ? (
            <tr><td colSpan="7" className="no-data">No routes found</td></tr>
          ) : (
            routes.map(route => (
              <tr key={route.id}>
                <td>{route.id}</td>
                <td>{route.name}</td>
                <td>{route.distance || 'N/A'}</td>
                <td>{route.creationDate}</td>
                <td>{route.from?.name || 'N/A'}</td>
                <td>{route.to?.name || 'N/A'}</td>
                <td className="actions">
                  <button className="btn-edit" onClick={() => handleEdit(route)}>Edit</button>
                  <button className="btn-delete" onClick={() => handleDelete(route.id)}>Delete</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      <div className="pagination">
        <button onClick={() => setPage(p => Math.max(0, p - 1))} disabled={page === 0}>Previous</button>
        <span>Page {page + 1}</span>
        <button onClick={() => setPage(p => p + 1)}>Next</button>
      </div>
    </div>
  );
};

export default RouteList;
