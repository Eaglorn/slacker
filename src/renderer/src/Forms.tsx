import { ChangeEvent, useState } from 'react'
import MySelect from './components/MySelect'

function Forms(): JSX.Element {
  const [inv, setInv] = useState<string>('')
  const [inputValue, setInputValue] = useState<string>('')
  const selectOptions = [
    { id: 0, name: 'Тип 1' },
    { id: 1, name: 'Тип 2' },
    { id: 2, name: 'Тип 3' },
    { id: 3, name: 'Не тип 1' },
    { id: 4, name: 'Тест' }
  ]

  const onChangeInputInv = (event: ChangeEvent<HTMLInputElement>) => {
    setInv(event.target.value)
  }

  const handleSelect = (name: string) => {
    setInputValue(name)
  }

  const buttonClick = () => {
    window.api.doThing(inv, inputValue)
  }

  return (
    <div className="flex h-screen items-center justify-center">
      <div className="rounded-xl border-2 border-indigo-500 p-3">
        <div className="flex h-32 w-48 flex-col justify-between">
          <div>
            <input
              type="text"
              value={inv}
              onChange={onChangeInputInv}
              className="mt-2 w-full rounded-none border-2 border-teal-950"
            />
            <div className="relative">
              <MySelect options={selectOptions} onSelect={handleSelect} />
            </div>
          </div>
        </div>
        <button onClick={buttonClick} className="w-full rounded-xl border-2 border-teal-950">
          Создать шаблон
        </button>
      </div>
    </div>
  )
}

export default Forms
