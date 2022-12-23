export function randomString(length = 16, radix = 36): string {
    const result = Math.floor(Math.random() * Number.MAX_SAFE_INTEGER)
        .toString(radix)
        .slice(0, length)

    if (result.length < length) {
        return `${result}${randomString(length - result.length, radix)}`
    }

    return result
}
