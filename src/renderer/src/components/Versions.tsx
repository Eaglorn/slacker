import { useState } from 'react'

function Versions(): JSX.Element {
  const [versions] = useState(window.electron.process.versions)

  return (
    <ul className="versions">
      <li className="electron-version text-orange-500 ">Electron v{versions.electron}</li>
      <li className="chrome-version text-sky-800">Chromium v{versions.chrome}</li>
      <li className="node-version text-fuchsia-800">Node v{versions.node}</li>
    </ul>
  )
}

export default Versions
