export default function generateRandomColor(input: string | number) {
    const stringInput = "" + input
    let hash = 0

    for (const char of stringInput) {
        hash = (char.codePointAt(0) ?? -1) + ((hash << 5) - hash)
    }

    hash = (hash * 16_807) % 2_147_483_647

    let colour = "#"
    for (let index = 0; index < 3; index++) {
        const value = (hash >> (index * 8)) & 0xff
        colour += ("00" + value.toString(16)).slice(-2)
    }
    return colour
}
