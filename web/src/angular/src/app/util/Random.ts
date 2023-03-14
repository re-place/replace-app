export default function generateRandomColor(input: string | number) {
    const stringInput = "" + input
    let hash = 0

    for (const char of stringInput) {
        hash = (char.codePointAt(0) ?? -1) + ((hash << 5) - hash)
    }

    hash = (hash * 16_807) % 2_147_483_647
    return `hsl(${hash % 360}, 70%, 50%)`
}
