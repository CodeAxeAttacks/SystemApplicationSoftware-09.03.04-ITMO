import matplotlib.pyplot as plt
import numpy as np

def manchester_encoding(bits):
    time = np.arange(0, len(bits) * 2, 1)
    signal = []
    
    for bit in bits:
        if bit == '1':
            signal.extend([1, 0])  # 1 -> High to Low
        else:
            signal.extend([0, 1])  # 0 -> Low to High
    
    return time, signal

def plot_manchester(bits):
    time, signal = manchester_encoding(bits)
    fig, ax = plt.subplots(figsize=(10, 2))
    
    ax.step(time, signal, where='mid', color='black', linewidth=2)
    ax.set_yticks([0, 1])
    ax.set_xticks(np.arange(0, len(bits) * 2, 2))
    ax.set_xticklabels(list(bits))
    
    for i, bit in enumerate(bits):
        ax.text(i * 2 + 0.5, 1.2, bit, fontsize=12, ha='center', color='blue' if bit == '1' else 'red')
    
    ax.grid(True, linestyle='--', alpha=0.6)
    ax.set_ylim(-0.5, 1.5)
    ax.set_xlim(0, len(bits) * 2 - 1)
    plt.title("Manchester Encoding")
    plt.show()

# Пример использования
binary_sequence = "110111001001001000010111"
plot_manchester(binary_sequence)
