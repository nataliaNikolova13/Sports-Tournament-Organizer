import React, { useState, useEffect } from "react";
import axios from "axios";
import "./CreateTeam.css";

const CreateTeam = () => {
  const [showForm, setShowForm] = useState(false);
  const [hideButton, setHideButton] = useState(true);
  const [categories, setCategories] = useState([]);
  const [formData, setFormData] = useState({
    name: "",
    category: "amateur",
  });

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get(
          "http://localhost:8080/teams/categories",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setCategories(response.data);
        console.log(response.data);
      } catch (error) {
        console.error("Error with fetching dropdown data", error);
      }
    };

    fetchCategories();
  }, [showForm]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleHide = () => {
    setShowForm(true);
    setHideButton(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem("token");
      const response = await axios.post(
        "http://localhost:8080/teams",
        formData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log("Team created:", response.data);
      setShowForm(false);
      setHideButton(true);
      setFormData({ name: "", category: "amateur" });
    } catch (error) {
      console.error("There was an error creating the team!", error);
    }
  };

  return (
    <div>
      {hideButton && <button onClick={handleHide}>Create Team</button>}

      {showForm && (
        <form onSubmit={handleSubmit} className="form-container">
          <div>
            <label htmlFor="name">Name:</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleInputChange}
              required
            />
          </div>
          <div>
            <label htmlFor="category">Category:</label>
            <select
              id="category"
              name="category"
              value={formData.category}
              onChange={handleInputChange}
              required
            >
              {categories.map((category) => (
                <option key={category} value={category}>
                  {category}
                </option>
              ))}
            </select>
          </div>
          <div className="buttons">
            <button type="submit">Submit</button>
            <button type="button" onClick={() => setShowForm(false)}>
              Cancel
            </button>
          </div>
        </form>
      )}
    </div>
  );
};

export default CreateTeam;
