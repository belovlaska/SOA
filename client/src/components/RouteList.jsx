import React, { useState, useEffect } from 'react';
import { getRoutes, deleteRoute } from '../services/apiService';
import ErrorDisplay from './ErrorDisplay';
import './RouteList.css';

const RouteList = ({ refresh, onEdit }) => {
  const [routes, setRoutes] = useState([]);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(100);
  const [sort, setSort] = useState('id');
  const [filters, setFilters] = useState({
    id: '',
    name: '',
    name_like: '',
    distance: '',
    distance_gt: '',
    distance_gte: '',
    distance_lt: '',
    distance_lte: '',
    creationDate_before: '',
    creationDate_after: '',
    fromName: '',
    fromName_like: '',
    toName: '',
    toName_like: '',
  });

  useEffect(() => {
    console.log('Filters changed:', filters);
    fetchRoutes();
  }, [refresh, page, size, sort]);

  const fetchRoutes = async () => {
    setLoading(true);
    setError(null);
    try {
      // Создаем параметры запроса
      const params = {
        page,
        size,
      };

      // Добавляем сортировку
      if (sort) {
        const sortMapping = {
          'id': 'id:asc',
          '-id': 'id:desc',
          'name': 'name:asc',
          '-name': 'name:desc',
          'distance': 'distance:asc',
          '-distance': 'distance:desc',
          'creationDate': 'creationDate:asc',
          '-creationDate': 'creationDate:desc',
        };
        params.sort = sortMapping[sort] || sort;
      }

      // Добавляем только заполненные фильтры
      Object.keys(filters).forEach(key => {
        const value = filters[key];
        if (value !== '' && value !== null && value !== undefined) {
          // Преобразуем числовые значения
          if (key === 'id' || key === 'distance' ||
              key === 'distance_gt' || key === 'distance_gte' ||
              key === 'distance_lt' || key === 'distance_lte') {
            const numValue = parseFloat(value);
            if (!isNaN(numValue)) {
              params[key] = numValue;
            }
          } else {
            params[key] = value;
          }
        }
      });

      console.log('API Request params:', params);
      const response = await getRoutes(params);
      console.log('API Response:', response.data);
      setRoutes(response.data.data || []);
    } catch (err) {
      console.error('API Error:', err);
      const errorData = err.response?.data || {
        code: err.response?.status || 500,
        message: err.response?.statusText || 'Failed to fetch routes'
      };
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
  };

  const handleSortChange = (e) => {
    setSort(e.target.value);
  };

  const handleEdit = (route) => {
    onEdit(route);
  };

  const handleApplyFilters = () => {
    setPage(0);
    fetchRoutes();
  };

  const handleResetFilters = () => {
    setFilters({
      id: '',
      name: '',
      name_like: '',
      distance: '',
      distance_gt: '',
      distance_gte: '',
      distance_lt: '',
      distance_lte: '',
      creationDate_before: '',
      creationDate_after: '',
      fromName: '',
      fromName_like: '',
      toName: '',
      toName_like: '',
    });
    setPage(0);
    setSort('id');
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="route-list-container">
      <h2>Routes List</h2>
      <ErrorDisplay error={error} />

      <div className="filters-section">
        <div className="filters-header">
          <h3>Filters & Sort</h3>
          <div className="filter-actions">
            <button
              className="btn btn--primary btn--sm"
              onClick={handleApplyFilters}
            >
              Apply Filters
            </button>
            <button
              className="btn btn--secondary btn--sm"
              onClick={handleResetFilters}
            >
              Reset All
            </button>
          </div>
        </div>

        <div className="filter-row">
          <div className="filter-group">
            <label>ID (exact):</label>
            <input
              type="number"
              name="id"
              placeholder="Filter by ID"
              value={filters.id}
              onChange={handleFilterChange}
              min="0"
            />
          </div>

          <div className="filter-group">
            <label>Name (exact):</label>
            <input
              type="text"
              name="name"
              placeholder="Exact name match"
              value={filters.name}
              onChange={handleFilterChange}
            />
          </div>

          <div className="filter-group">
            <label>Name (contains):</label>
            <input
              type="text"
              name="name_like"
              placeholder="Name contains"
              value={filters.name_like}
              onChange={handleFilterChange}
            />
          </div>

          <div className="filter-group">
            <label>Distance (exact):</label>
            <input
              type="number"
              name="distance"
              placeholder="Exact distance"
              value={filters.distance}
              onChange={handleFilterChange}
              min="0"
              step="any"
            />
          </div>
        </div>

        <div className="filter-row">
          <div className="filter-group">
            <label>Distance &gt;:</label>
            <input
              type="number"
              name="distance_gt"
              placeholder="Greater than"
              value={filters.distance_gt}
              onChange={handleFilterChange}
              min="0"
              step="any"
            />
          </div>

          <div className="filter-group">
            <label>Distance &gt;=:</label>
            <input
              type="number"
              name="distance_gte"
              placeholder="Greater or equal"
              value={filters.distance_gte}
              onChange={handleFilterChange}
              min="0"
              step="any"
            />
          </div>

          <div className="filter-group">
            <label>Distance &lt;:</label>
            <input
              type="number"
              name="distance_lt"
              placeholder="Less than"
              value={filters.distance_lt}
              onChange={handleFilterChange}
              min="0"
              step="any"
            />
          </div>

          <div className="filter-group">
            <label>Distance &lt;=:</label>
            <input
              type="number"
              name="distance_lte"
              placeholder="Less or equal"
              value={filters.distance_lte}
              onChange={handleFilterChange}
              min="0"
              step="any"
            />
          </div>
        </div>

        <div className="filter-row">
          <div className="filter-group">
            <label>Created After:</label>
            <input
              type="date"
              name="creationDate_after"
              value={filters.creationDate_after}
              onChange={handleFilterChange}
            />
          </div>

          <div className="filter-group">
            <label>Created Before:</label>
            <input
              type="date"
              name="creationDate_before"
              value={filters.creationDate_before}
              onChange={handleFilterChange}
            />
          </div>

          <div className="filter-group">
            <label>From Name (exact):</label>
            <input
              type="text"
              name="fromName"
              placeholder="Exact from location"
              value={filters.fromName}
              onChange={handleFilterChange}
            />
          </div>

          <div className="filter-group">
            <label>From Name (contains):</label>
            <input
              type="text"
              name="fromName_like"
              placeholder="From name contains"
              value={filters.fromName_like}
              onChange={handleFilterChange}
            />
          </div>
        </div>

        <div className="filter-row">
          <div className="filter-group">
            <label>To Name (exact):</label>
            <input
              type="text"
              name="toName"
              placeholder="Exact to location"
              value={filters.toName}
              onChange={handleFilterChange}
            />
          </div>

          <div className="filter-group">
            <label>To Name (contains):</label>
            <input
              type="text"
              name="toName_like"
              placeholder="To name contains"
              value={filters.toName_like}
              onChange={handleFilterChange}
            />
          </div>

          <div className="filter-group">
            <label>Sort by:</label>
            <select className="form-control" value={sort} onChange={handleSortChange}>
              <option value="id">ID (Ascending)</option>
              <option value="-id">ID (Descending)</option>
              <option value="name">Name (Ascending)</option>
              <option value="-name">Name (Descending)</option>
              <option value="distance">Distance (Ascending)</option>
              <option value="-distance">Distance (Descending)</option>
              <option value="creationDate">Creation Date (Ascending)</option>
              <option value="-creationDate">Creation Date (Descending)</option>
            </select>
          </div>

          <div className="filter-group">
            <label>Items per page:</label>
            <select
              className="form-control"
              value={size}
              onChange={(e) => {
                setSize(parseInt(e.target.value));
                setPage(0);
              }}
            >
              <option value="5">5</option>
              <option value="10">10</option>
              <option value="20">20</option>
              <option value="50">50</option>
            </select>
          </div>
        </div>
      </div>

      <div className="debug-info" style={{ margin: '10px 0', fontSize: '12px', color: '#666' }}>
        <strong>Debug:</strong> Filters applied: {Object.keys(filters).filter(k => filters[k] !== '').length}
      </div>

      <div className="table-container">
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
      </div>

      <div className="pagination">
        <button
          onClick={() => setPage(p => Math.max(0, p - 1))}
          disabled={page === 0}
          className="btn btn--secondary"
        >
          Previous
        </button>
        <span className="page-info">Page {page + 1}</span>
        <button
          onClick={() => setPage(p => p + 1)}
          className="btn btn--secondary"
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default RouteList;