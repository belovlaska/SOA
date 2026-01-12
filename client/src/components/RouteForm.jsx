import React, { useState, useEffect } from 'react';
import { createRoute, updateRoute } from '../services/apiService';
import ErrorDisplay from './ErrorDisplay';
import './RouteForm.css';

const RouteForm = ({ route, onSave, onCancel }) => {
  const [formData, setFormData] = useState({
    name: '',
    distance: '',
    coordinates: { x: 0, y: 0 },
    from: { x: 0, y: 0, z: 0, name: '' },
    to: { x: 0, y: 0, z: 0, name: '' },
  });
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (route) {
      setFormData(route);
    }
  }, [route]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === 'distance' ? (value ? parseInt(value) : '') : value,
    }));
  };

  const handleCoordinatesChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      coordinates: {
        ...prev.coordinates,
        [name]: name === 'x' ? parseInt(value) : parseFloat(value),
      },
    }));
  };

  const handleLocationChange = (location, field, e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [location]: {
        ...prev[location],
        [name]: ['x', 'y', 'z'].includes(name) ? parseInt(value) : value,
      },
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      if (formData.id) {
        await updateRoute(formData.id, formData);
      } else {
        await createRoute(formData);
      }
      onSave();
    } catch (err) {
      const errorData = err.response?.data || { code: 500, message: 'Failed to save route' };
      setError(errorData);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="route-form-container">
      <h2>{formData.id ? 'Edit Route' : 'Create New Route'}</h2>
      <ErrorDisplay error={error} />

      <form onSubmit={handleSubmit}>
        <div className="form-section">
          <h3>Route Info</h3>
          
          <div className="form-group">
            <label>Name *</label>
            <input
              type="text"
              name="name"
              value={formData.name || ''}
              onChange={handleInputChange}
              required
              placeholder="Enter route name"
            />
          </div>

          <div className="form-group">
            <label>Distance</label>
            <input
              type="number"
              name="distance"
              value={formData.distance || ''}
              onChange={handleInputChange}
              min="2"
              placeholder="Enter distance (min 2)"
            />
          </div>
        </div>

        <div className="form-section">
          <h3>Coordinates</h3>
          
          <div className="form-group">
            <label>X *</label>
            <input
              type="number"
              name="x"
              value={formData.coordinates?.x || 0}
              onChange={handleCoordinatesChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Y *</label>
            <input
              type="number"
              name="y"
              value={formData.coordinates?.y || 0}
              onChange={handleCoordinatesChange}
              step="0.1"
              required
            />
          </div>
        </div>

        <div className="form-section">
          <h3>From Location</h3>
          
          <div className="form-group">
            <label>From X *</label>
            <input
              type="number"
              name="x"
              value={formData.from?.x || 0}
              onChange={(e) => handleLocationChange('from', 'x', e)}
              required
            />
          </div>

          <div className="form-group">
            <label>From Y *</label>
            <input
              type="number"
              name="y"
              value={formData.from?.y || 0}
              onChange={(e) => handleLocationChange('from', 'y', e)}
              required
            />
          </div>

          <div className="form-group">
            <label>From Z *</label>
            <input
              type="number"
              name="z"
              value={formData.from?.z || 0}
              onChange={(e) => handleLocationChange('from', 'z', e)}
              required
            />
          </div>

          <div className="form-group">
            <label>From Name *</label>
            <input
              type="text"
              name="name"
              value={formData.from?.name || ''}
              onChange={(e) => handleLocationChange('from', 'name', e)}
              required
              placeholder="Enter from location name"
            />
          </div>
        </div>

        <div className="form-section">
          <h3>To Location</h3>
          
          <div className="form-group">
            <label>To X *</label>
            <input
              type="number"
              name="x"
              value={formData.to?.x || 0}
              onChange={(e) => handleLocationChange('to', 'x', e)}
              required
            />
          </div>

          <div className="form-group">
            <label>To Y *</label>
            <input
              type="number"
              name="y"
              value={formData.to?.y || 0}
              onChange={(e) => handleLocationChange('to', 'y', e)}
              required
            />
          </div>

          <div className="form-group">
            <label>To Z *</label>
            <input
              type="number"
              name="z"
              value={formData.to?.z || 0}
              onChange={(e) => handleLocationChange('to', 'z', e)}
              required
            />
          </div>

          <div className="form-group">
            <label>To Name *</label>
            <input
              type="text"
              name="name"
              value={formData.to?.name || ''}
              onChange={(e) => handleLocationChange('to', 'name', e)}
              required
              placeholder="Enter to location name"
            />
          </div>
        </div>

        <div className="form-actions">
          <button type="submit" disabled={loading} className="btn-submit">
            {loading ? 'Saving...' : formData.id ? 'Update Route' : 'Create Route'}
          </button>
          <button type="button" onClick={onCancel} className="btn-cancel">
            Cancel
          </button>
        </div>
      </form>
    </div>
  );
};

export default RouteForm;
