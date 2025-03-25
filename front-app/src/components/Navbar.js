// src/components/Navbar.js
import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav style={{ padding: '10px', backgroundColor: '#282c34' }}>
      <Link to="/" style={{ margin: '0 10px', color: 'white' }}>Login</Link>
      <Link to="/cadastro" style={{ margin: '0 10px', color: 'white' }}>Cadastro</Link>
      <Link to="/dashboard" style={{ margin: '0 10px', color: 'white' }}>Dashboard</Link>
      <Link to="/depoimentos" style={{ margin: '0 10px', color: 'white' }}>Depoimentos</Link>
      {/* Adicione os links para outras páginas aqui */}
    </nav>
  );
}

export default Navbar;
