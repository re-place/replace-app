
export type LocalStorageValue<T> = {
    get value(): T
    set value(newValue: T)
}

export function useLocalStorage<T>(key: string): LocalStorageValue<T | undefined>
export function useLocalStorage<T>(key: string, defaultValue: T): LocalStorageValue<T>

export function useLocalStorage<T>(key: string, defaultValue?: T)  {
    const localStorageValue = localStorage.getItem(key)

    if (localStorageValue === null && defaultValue !== undefined) {
        localStorage.setItem(key, JSON.stringify(defaultValue))
    }

    return {
        get value() {
            const val = localStorage.getItem(key)

            if (val === null) {
                return defaultValue as T
            }

            return JSON.parse(val)
        },

        set value(newValue: T) {
            if (newValue === undefined) {
                localStorage.removeItem(key)
                return
            }

            localStorage.setItem(key, JSON.stringify(newValue))
        },
    }
}
