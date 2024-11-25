import { useState, useRef } from 'react'

type Option = {
  id: number
  name: string
}

type SelectProps = {
  options: Option[]
  onSelect: Function
}

function MySelect(props: Readonly<SelectProps>): JSX.Element {
  const options: Option[] = props.options
  const onSelect = props.onSelect

  const [inputValue, setInputValue] = useState<string>('')
  const [filteredList, setFilteredList] = useState<Option[]>([])
  const [isDropdownOpen, setIsDropdownOpen] = useState<boolean>(false)
  const dropdownRef = useRef<HTMLUListElement | null>(null)

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value
    setInputValue(value)

    if (value) {
      const filteredOptions = options.filter((option) =>
        option.name.toLowerCase().includes(value.toLowerCase())
      )
      setFilteredList(filteredOptions)
      setIsDropdownOpen(true)
    } else {
      setFilteredList([])
      setIsDropdownOpen(false)
    }
  }

  const handleBlur = (event: React.FocusEvent<HTMLInputElement>) => {
    if (dropdownRef.current && !dropdownRef.current.contains(event.relatedTarget as Node)) {
      setIsDropdownOpen(false)
    }
  }

  const handleFocus = () => {
    if (inputValue) {
      setIsDropdownOpen(true)
    }
  }

  const handleSelect = (option: Option) => {
    setInputValue(option.name)
    setFilteredList([])
    setIsDropdownOpen(false)
    if (onSelect) {
      onSelect(option.name)
    }
  }

  return (
    <>
      <input
        type="text"
        value={inputValue}
        className="mt-2 w-full rounded-none border-2 border-teal-950"
        onChange={handleChange}
        onFocus={handleFocus}
        onBlur={handleBlur}
      />
      {isDropdownOpen && filteredList.length > 0 && (
        <ul
          className="absolute left-0 right-0 z-50 max-h-44 overflow-auto border-2 border-t-0 border-solid border-gray-300 bg-white"
          ref={dropdownRef}
        >
          {filteredList.map((option) => (
            <li key={option.id} className="mt-1 cursor-pointer hover:bg-gray-200">
              <button onClick={() => handleSelect(option)} className="w-full text-left">
                {option.name}
              </button>
            </li>
          ))}
        </ul>
      )}
    </>
  )
}

export default MySelect
