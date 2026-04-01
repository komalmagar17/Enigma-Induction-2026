from transformers import pipeline

def summarize_text(input_text):
    """
    Summarizes the input text using Hugging Face's pre-trained summarization pipeline.
    """
    print("Loading summarization model...")
    # Initialize the summarization pipeline
    summarizer = pipeline("summarization")
    
    print("Generating summary...")
    # Generate summary
    summary = summarizer(input_text, max_length=50, min_length=20, do_sample=False)
    
    return summary[0]['summary_text']

if __name__ == "__main__":
    # Example long paragraph
    text_to_summarize = """
    Artificial Intelligence is transforming many industries by allowing machines to perform tasks 
    that normally require human intelligence. It is widely used in healthcare, finance, robotics, 
    and automation. As AI technology continues to evolve, it is expected to create new opportunities 
    for innovation while also posing significant challenges in terms of ethics, security, and 
    the future of work. Understanding the implications of AI is crucial for both developers and 
    policy makers to ensure that it benefits society as a whole.
    """
    
    print("\n--- Original Text ---")
    print(text_to_summarize.strip())
    
    summary_result = summarize_text(text_to_summarize)
    
    print("\n--- Generated Summary ---")
    print(summary_result)
