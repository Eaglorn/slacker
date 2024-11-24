import { SetStateAction, useState } from 'react'

type Option = {
  id: number
  name: string
}

type SelectProps = {
  options: Option[]
}

function MySelect(props: Readonly<SelectProps>): JSX.Element {
  const options: Option[] = props.options

  const [inputValue, setInputValue] = useState<string>('')
  const [selectedOption, setSelectedOption] = useState<Option>()

  const filteredOptions = options.filter((option) =>
    option.name.toLowerCase().includes(inputValue.toLowerCase())
  )

  const handleSelect = (option: Option) => {
    setSelectedOption(option)
    if (option) setInputValue(option.name)
  }

  return (
    <>
      <input
        type="text"
        value={inputValue}
        className="mt-2 w-full rounded-none border-2 border-teal-950"
        onChange={(e) => setInputValue(e.target.value)}
      />
      {filteredOptions.length > 0 && (
        <ul className="dropdown max-h-44 overflow-auto">
          {filteredOptions.map((option) => (
            <li key={option.id} onClick={() => handleSelect(option)} className="">
              {option.name}
            </li>
          ))}
        </ul>
      )}
    </>
  )
}

export default MySelect
