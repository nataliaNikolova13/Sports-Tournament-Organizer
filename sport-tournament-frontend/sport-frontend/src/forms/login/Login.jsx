import React, { useState, useEffect } from "react";
import axios from "axios";
import "./Login.css";
import { Link, useNavigate } from "react-router-dom";

const LoginForm = () => {
  const initialFormState = {
    email: "",
    password: "",
  };
  const [formData, setFormData] = useState(initialFormState);

  const [errors, setErrors] = useState({});
  const [loginError, setLoginError] = useState("");
  const [isLogged, setIsLogged] = useState(false);
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      setIsLogged(true);
    } else {
      setIsLogged(false);
    }
  }, [localStorage.getItem("token")]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const validate = () => {
    const errors = {};
    if (!formData.email) {
      errors.email = "Email is required";
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      errors.email = "Email address is invalid";
    }
    if (!formData.password) errors.password = "Password is required";
    return errors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
    } else {
      try {
        const response = await axios.post(
          "http://localhost:8080/auth/login",
          formData
        );
        console.log("Form Data Submitted", response.data);
        localStorage.setItem("token", response.data.token);
        clear();
        setIsLogged(true);
        navigate("/");
      } catch (error) {
        setLoginError("No such user");
      }
    }
  };

  const clear = () => {
    setFormData(initialFormState);
    setErrors({});
    setLoginError("");
  };

  return (
    <div className="container">
      {isLogged ? (
        <h2>Welcome to Sports Tournament</h2>
      ) : (
        <>
          <h2>Login Form</h2>
          <form onSubmit={handleSubmit}>
            <div>
              <label>Email:</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
              />
              {errors.email && <p className="error">{errors.email}</p>}
            </div>
            <div>
              <label>Password:</label>
              <input
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
              />
              {errors.password && <p className="error">{errors.password}</p>}
            </div>
            <button type="submit">Login</button>
            {loginError && <p className="error">{loginError}</p>}
            <Link to="/registration">Register</Link>
          </form>
        </>
      )}
    </div>
  );
};

export default LoginForm;
