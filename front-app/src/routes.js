import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Login from "./pages/Login";
import Cadastro from "./pages/Cadastro";
import Home from "./pages/Home.js";
import Depoimentos from "./pages/Depoimentos";
import GraficoCargos from "./pages/GraficoCargos";
import Vagas from "./pages/Vagas";
import Chat from "./pages/Chat";
import PrivateRoute from "./routes/PrivateRoute";
import CadastroEgresso from "./pages/CadastroEgresso";
import Grupos from "./pages/Grupos";
import HomeCoordenador from "./pages/HomeCoordenador.js";

export default function AppRoutes() {
  return (
    <Router>
      <Routes>
        {/* Página inicial pública agora é o gráfico */}
        <Route path="/" element={<GraficoCargos />} />

        {/* Rota pública de login */}
        <Route path="/login" element={<Login />} />

        {/* Rotas protegidas */}
        <Route element={<PrivateRoute />}>
          <Route path="/home" element={<Home />} />
          <Route path="/homeCoordenador" element={<HomeCoordenador />} />
          <Route path="/depoimentos" element={<Depoimentos />} />
          <Route path="/vagas" element={<Vagas />} />
          <Route path="/grupos" element={<Grupos />} />
          <Route path="/chat/:grupoId" element={<Chat />} />
        </Route>
      </Routes>
    </Router>
  );
}
