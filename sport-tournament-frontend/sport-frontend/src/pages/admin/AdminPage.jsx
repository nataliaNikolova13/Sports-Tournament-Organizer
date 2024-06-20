import axios from "axios";
import "./AdminPage.css";
import { Link } from "react-router-dom";
import React, { useState, useEffect } from "react";

const AdminPage = ({ decodeToken }) => {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState("");
  const [selectedRole, setSelectedRole] = useState("");
  const [roles] = useState(["Organizer", "Admin"]);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const token = localStorage.getItem("token");
        const response = await axios.get("http://localhost:8080/user", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        setUsers(response.data);
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    };

    fetchUsers();
  }, []);

  const handleChangeUser = (event) => {
    setSelectedUser(event.target.value);
  };

  const handleChangeRole = (event) => {
    setSelectedRole(event.target.value);
  };

  const handleSubmit = async (event) => {
    console.log(selectedUser);
    console.log(users);
    event.preventDefault();
    try {
      const token = localStorage.getItem("token");
      const response = await axios.put(
        `http://localhost:8080/user/role/${selectedUser}`,
        { role: selectedRole },
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log("User role updated:", response.data);
    } catch (error) {
      console.error("Error updating user role:", error);
    }
  };
  return (
    <div className="admin-page-container">
      <h2>Admin Page</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Select User:</label>
          <select value={selectedUser} onChange={handleChangeUser} required>
            <option value="">Select a user</option>
            {users.map((user) => (
              <option key={user.id} value={user.id}>
                {user.fullName}
              </option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label>Select Role:</label>
          <select value={selectedRole} onChange={handleChangeRole} required>
            <option value="">Select a role</option>
            {roles.map((role) => (
              <option key={role} value={role}>
                {role}
              </option>
            ))}
          </select>
        </div>
        <button type="submit">Update Role</button>
      </form>
    </div>
  );
};

export default AdminPage;
