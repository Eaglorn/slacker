import Versions from './components/MySelect'
import electronLogo from './assets/electron.svg'
import { types, getSnapshot } from 'mobx-state-tree'
//import { observer } from 'mobx-react-lite'

const Todo = types
  .model({
    name: types.optional(types.string, ''),
    done: types.optional(types.boolean, false)
  })
  .actions((self) => {
    function setName(newName) {
      self.name = newName
    }

    function toggle() {
      self.done = !self.done
    }

    return { setName, toggle }
  })

const User = types.model({
  name: types.optional(types.string, '')
})

const RootStore = types
  .model({
    users: types.map(User),
    todos: types.optional(types.map(Todo), {})
  })
  .actions((self) => {
    function addTodo(id, name) {
      self.todos.set(id, Todo.create({ name }))
    }

    return { addTodo }
  })

const store = RootStore.create({
  users: {} // users is required here because it's not marked as optional
})

function App(): JSX.Element {
  const ipcHandle = (): void => window.electron.ipcRenderer.send('ping')

  return (
    <>
      <img alt="logo" className="logo" src={electronLogo} />
      <div className="creator">Powered by electron-vite</div>
      <div className="text">
        Build an Electron app with <span className="react">React</span> &nbsp;and{' '}
        <span className="ts">TypeScript</span>
      </div>
      <p className="tip">
        Please try pressing <code>F12</code> to open the devTool
      </p>
      <div className="actions">
        <div className="action">
          <a href="https://electron-vite.org/" target="_blank" rel="noreferrer">
            Documentation
          </a>
        </div>
        <div className="action">
          <a target="_blank" rel="noreferrer" onClick={ipcHandle}>
            Send IPC
          </a>
        </div>
      </div>
      <Versions></Versions>
    </>

}

export default App
