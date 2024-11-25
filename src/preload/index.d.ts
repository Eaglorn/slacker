import { ElectronAPI } from '@electron-toolkit/preload'

declare global {
  interface Window {
    electron: ElectronAPI
    api: {
      doThing: (inv: string, inputValue: string) => void
    }
  }
}
