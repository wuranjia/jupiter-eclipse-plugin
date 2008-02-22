package edu.hawaii.ics.csdl.jupiter.model.review;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Provides review id information.
 * 
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewId {
  private String reviewId;
  private String description;
  private String author;
  private String directory;
  private Map<String, ReviewerId> reviewers;
  private Map<String, List<String>> categoryMap;
  private Date date;

  /**
   * Instantiates this instance.
   * 
   * @param reviewIdName the review id name.
   * @param description the description of the review id.
   * @param author the author of the review session.
   * @param directory the <code>String </code> relative review directory from a project root.
   * @param reviewers the reviewer map of <code>ReviewerId</code> instances.
   * @param categoryMap the category map.
   * @param creationDate the creation date.
   */
  public ReviewId(String reviewIdName, String description, String author, String directory,
      Map<String, ReviewerId> reviewers, Map<String, List<String>> categoryMap,
      Date creationDate) {
    this.reviewId = reviewIdName;
    this.description = description;
    this.author = author;
    this.directory = directory;
    this.reviewers = reviewers;
    this.categoryMap = categoryMap;
    this.date = creationDate;
  }

  /**
   * Gets category map.
   * 
   * @return Returns the categoryMap.
   */
  public Map<String, List<String>> getCategoryMap() {
    return this.categoryMap;
  }

  /**
   * Sets category map.
   * 
   * @param categoryMap the category map.
   */
  public void setCategoryMap(Map<String, List<String>> categoryMap) {
    this.categoryMap = categoryMap;
  }

  /**
   * Gets reviewers.
   * 
   * @return Returns the reviewers.
   */
  public Map<String, ReviewerId> getReviewers() {
    return this.reviewers;
  }

  /**
   * Sets reviewer map.
   * 
   * @param reviewers the reviewer map.
   */
  public void setReviewers(Map<String, ReviewerId> reviewers) {
    this.reviewers = reviewers;
  }

  /**
   * Gets review id name.
   * 
   * @return Returns the reviewIdName.
   */
  public String getReviewId() {
    return this.reviewId;
  }

  /**
   * Gets the description of the review id.
   * 
   * @return the description of the review id.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Sets the description of the review ID.
   * 
   * @param description the description of the review ID.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Gets the <code>String </code> relative review directory from a project root.
   * 
   * @return the <code>String </code> relative review directory from a project root.
   */
  public String getDirectory() {
    return this.directory;
  }

  /**
   * Sets the <code>String </code> relative review directory from a project root.
   * 
   * @param directory the review directory in which review files are stored.
   */
  public void setDirectory(String directory) {
    this.directory = directory;
  }

  /**
   * Gets date.
   * 
   * @return Returns the date.
   */
  public Date getDate() {
    return date;
  }

  /**
   * Gets the author of the review session.
   * 
   * @return Returns the author of the review session.
   */
  public String getAuthor() {
    return this.author;
  }

  /**
   * Sets the author of the review session.
   * 
   * @param author the author of the review session.
   */
  public void setAuthor(String author) {
    this.author = author;
  }
}
