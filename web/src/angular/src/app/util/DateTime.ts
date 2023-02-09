export function toLocaleDateTimeString(date: string | undefined, options?: Intl.DateTimeFormatOptions) {
    if (date === undefined) {
        return undefined
    }

    return new Date(date).toLocaleString(undefined, options)
}

export function toLocaleDateString(date: string | undefined, options?: Intl.DateTimeFormatOptions) {
    if (date === undefined) {
        return undefined
    }

    return new Date(date).toLocaleDateString(undefined, options)
}

export function toLocaleTimeString(date: string | undefined, options?: Intl.DateTimeFormatOptions) {
    if (date === undefined) {
        return undefined
    }

    return new Date(date).toLocaleTimeString(undefined, options)
}
