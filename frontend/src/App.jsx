import { useState, useEffect } from 'react';
import './App.css';

const API_URL = `${import.meta.env.VITE_API_BASE_URL}/tasks`;

// Requisições centralizadas
async function apiRequest(url, method = 'GET', body) {
  const options = { method, headers: { 'Content-Type': 'application/json' } };
  if (body) options.body = JSON.stringify(body);

  const res = await fetch(url, options);
  if (!res.ok) throw new Error(`Erro na requisição: ${res.statusText}`);
  return res.json().catch(() => null); // evita erro em respostas vazias
}

// ================= COMPONENTES =================
function TaskForm({ onSubmit, formData, setFormData, editingTaskId }) {
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <form onSubmit={onSubmit} className="task-form">
      <h3>{editingTaskId ? 'Editar Tarefa' : 'Adicionar Nova Tarefa'}</h3>
      <input
        type="text"
        name="title"
        value={formData.title}
        onChange={handleChange}
        placeholder="Título da tarefa"
        required
      />
      <textarea
        name="description"
        value={formData.description}
        onChange={handleChange}
        placeholder="Descrição"
      />
      <input
        type="datetime-local"
        name="dueDate"
        value={formData.dueDate}
        onChange={handleChange}
      />
      <select name="priority" value={formData.priority} onChange={handleChange}>
        <option value="1">Prioridade 1 (Alta)</option>
        <option value="2">Prioridade 2</option>
        <option value="3">Prioridade 3 (Normal)</option>
        <option value="4">Prioridade 4</option>
        <option value="5">Prioridade 5 (Baixa)</option>
      </select>
      <button type="submit">
        {editingTaskId ? 'Salvar Alterações' : 'Adicionar Tarefa'}
      </button>
    </form>
  );
}

function TaskItem({ task, onToggle, onEdit, onDelete }) {
  return (
    <div
      className={`task-item ${task.completed ? 'completed' : ''} priority-${task.priority}`}
    >
      <div className="task-content">
        <h3>{task.title}</h3>
        <p>{task.description}</p>

        <div className="task-meta">
          <small><strong>Prazo:</strong> {task.dueDate ? new Date(task.dueDate).toLocaleString('pt-BR') : 'N/A'}</small>
          <small><strong>Prioridade:</strong> {task.priority}</small>
        </div>
      </div>

      <div className="task-actions">
        <button onClick={() => onToggle(task)}>
          {task.completed ? 'Reabrir' : 'Concluir'}
        </button>
        <button onClick={() => onEdit(task)}>Editar</button>
        <button onClick={() => onDelete(task.id)} className="delete-btn">
          Excluir
        </button>
      </div>
    </div>
  );
}



function TaskList({ tasks, onToggle, onEdit, onDelete }) {
  return (
    <div className="task-list">
      <h2>Minhas Tarefas</h2>
      {tasks.map((task) => (
        <TaskItem
          key={task.id}
          task={task}
          onToggle={onToggle}
          onEdit={onEdit}
          onDelete={onDelete}
        />
      ))}
    </div>
  );
}

// ================= APP =================
function App() {
  const [tasks, setTasks] = useState([]);
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    priority: 3,
    dueDate: ''
  });
  const [editingTaskId, setEditingTaskId] = useState(null);

  // Buscar tarefas
  const fetchTasks = async () => {
    try {
      const data = await apiRequest(API_URL);
      setTasks(data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, []);

  // Salvar (create/update)
  const handleSubmit = async (e) => {
    e.preventDefault();
    const taskData = {
      ...formData,
      dueDate: formData.dueDate
        ? new Date(formData.dueDate).toISOString()
        : null,
      priority: parseInt(formData.priority, 10)
    };

    try {
      if (editingTaskId) {
        await apiRequest(`${API_URL}/${editingTaskId}`, 'PUT', taskData);
      } else {
        await apiRequest(API_URL, 'POST', taskData);
      }
      setFormData({ title: '', description: '', priority: 3, dueDate: '' });
      setEditingTaskId(null);
      fetchTasks();
    } catch (err) {
      console.error(err);
    }
  };

  // Editar
  const handleEdit = (task) => {
    setEditingTaskId(task.id);
    setFormData({
      title: task.title,
      description: task.description,
      priority: task.priority,
      dueDate: task.dueDate ? task.dueDate.slice(0, 16) : ''
    });
  };

  // Excluir
  const handleDelete = async (id) => {
    try {
      await apiRequest(`${API_URL}/${id}`, 'DELETE');
      setTasks((prev) => prev.filter((t) => t.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  // Concluir / Reabrir
  const handleToggleComplete = async (task) => {
    try {
      await apiRequest(`${API_URL}/${task.id}`, 'PUT', {
        ...task,
        completed: !task.completed
      });
      fetchTasks();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="container">
      <h1>Gerenciador de Tarefas</h1>
      <TaskForm
        onSubmit={handleSubmit}
        formData={formData}
        setFormData={setFormData}
        editingTaskId={editingTaskId}
      />
      <TaskList
        tasks={tasks}
        onToggle={handleToggleComplete}
        onEdit={handleEdit}
        onDelete={handleDelete}
      />
    </div>
  );
}

export default App;
