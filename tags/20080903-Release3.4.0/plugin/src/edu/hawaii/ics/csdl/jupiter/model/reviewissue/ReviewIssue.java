package edu.hawaii.ics.csdl.jupiter.model.reviewissue;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import edu.hawaii.ics.csdl.jupiter.ReviewException;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewId;
import edu.hawaii.ics.csdl.jupiter.model.review.ReviewModel;

/**
 * Provides code review event data. This is contained in the <code>ReviewIssueModel</code>
 * instance.
 *
 * @author Takuya Yamashita
 * @version $Id$
 */
public class ReviewIssue {
  /**
   * The IFile implementing class instance to hold the review reviewIFile path (whose extension is
   * review) corresponding the <code>ReviewIssue</code> instance.
   */
  private IFile reviewIFile;
  /** The identification for this code review. */
  private String id;
  /** The creation date. */
  private Date creationDate;
  /** The modification date. */
  private Date modificationDate;
  /** The reviewer's name. */
  private String reviewer;
  /** The assignedTo's name. */
  private String assignedTo;
  /** The summary for this code review. */
  private String summary;
  /** The reviewIFile path for reviewing reviewIFile. */
  private String reviewingFilePath;
  /** The type if it's defect. */
  private Type type;
  /** The severity. */
  private Severity severity;
  /** The description of this code review. */
  private String description;
  /** The annotation for this review in the group review phase. */
  private String annotation;
  /** The revision comment for the annotation and review in the revision phase. */
  private String revision;
  /** The resolution if it's fix. */
  private Resolution resolution;
  /** The status if it's solved. */
  private Status status;
  /** The target file to be reviewed. */
  private String targetFile;
  /** The line number. Can be empty. */
  private String line = "";
  /** The <code>SimpleDateFormat</code> instance to format for id. */
  private SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
  /** The previous java hash to compose of fullyQualifiedClass, method and line. */
  private int previousJavaHashCode;
  /** The current java hash to compose of fullyQualifiedClass, method and line. */
  private int currentJavaHashCode;
  /**
   * The boolean status for the flag to see this is linked to a source code.
   * This is instantiated as <code>false</code> at the creation of this instance.
   * The status will be set when view image label provider iterate this instance.
   */
  private boolean isLinked;

  /**
   * Constructor for the ReviewIssue object. Instantiates every fields.
   * @param creationDate the creation date
   * @param modificationDate the modification date
   * @param reviewer the reviewer's name
   * @param assignedTo the person name who is assigned to
   * @param targetFile the target file
   * @param line the line number
   * @param type the type if it's defect
   * @param severity the severity.
   * @param summary the summary of the review.
   * @param description the description of this code review
   * @param annotation the annotation for this review
   * @param revision the revision comment for the annotation and review
   * @param resolution the resolution if it's fix
   * @param status the status if it's solved
   * @param reviewIFile the review <code>IFile</code> instance
   *
   * @throws ReviewException if problems occur.
   */
  public ReviewIssue(Date creationDate, Date modificationDate, String reviewer, String assignedTo,
    String targetFile, String line, Type type, Severity severity, String summary,
    String description, String annotation, String revision, Resolution resolution, Status status,
    IFile reviewIFile) throws ReviewException {
    this.setReviewIFile(reviewIFile);
    this.setCreationDate(creationDate);
    this.setModificationDate(modificationDate);
    this.setReviewer(reviewer);
    this.setAssignedTo(assignedTo);
    this.setTargetFile(targetFile);
    this.setLine(line);
    this.setType(type);
    this.setSeverity(severity);
    this.setSummary(summary);
    this.setDescription(description);
    this.setAnnotation(annotation);
    this.setRevision(revision);
    this.setResolution(resolution);
    this.setStatus(status);
    // Used for calculation if java hash code is changed.
    this.previousJavaHashCode = getJavaHashCode();
    this.currentJavaHashCode = getJavaHashCode();
  }
 

  /**
   * Gets the java hash code from <code>fullyQualifiedClass</code>, <code>method</code>, and
   * <code>line</code>.
   *
   * @return the java hash code.
   */
  private int getJavaHashCode() {
    int hash = 17;
    hash = (37 * hash) + this.line.hashCode();
    return hash;
  }

  /**
   * Checks if the dynamic link to a source code is updated.
   *
   * @return <code>true</code> if java hash code is updated.
   */
  public boolean isJavaHashCodeUpdated() {
    return (this.previousJavaHashCode != this.currentJavaHashCode);
  }

  /**
   * set the content of the passing <code>ReviewIssue</code> instance. Note that
   * the creationDate field would not be updated so that the id of this instance would not
   * be changed.
   *
   * @param codeReview the previous instance.
   *
   * @throws ReviewException if problems occur.
   */
  public void setReviewIssue(ReviewIssue codeReview) throws ReviewException {
    setReviewIFile(codeReview.getReviewIFile());
    setModificationDate(codeReview.getModificationDate());
    setReviewer(codeReview.getReviewer());
    setAssignedTo(codeReview.getAssignedTo());
    setTargetFile(codeReview.getTargetFile());
    setLine(codeReview.getLine());
    setType(codeReview.getType());
    setSeverity(codeReview.getSeverity());
    setSummary(codeReview.getSummary());
    setDescription(codeReview.getDescription());
    setAnnotation(codeReview.getAnnotation());
    setRevision(codeReview.getRevision());
    setResolution(codeReview.getResolution());
    setStatus(codeReview.getStatus());
    setLinkStatus(codeReview.isLinked());
    // Used for calculation if java hash code is changed.
    this.previousJavaHashCode = getJavaHashCode();
    this.currentJavaHashCode = getJavaHashCode();
  }

  /**
   * Gets the <code>IFile</code> instance to hold the reviewIFile path associated with the code
   * review.
   *
   * @return The <code>IFile</code> implementing class instance.
   */
  public IFile getReviewIFile() {
    return this.reviewIFile;
  }

  /**
   * Sets the <code>IFile</code> instance to hold the reviewIFile path associated with the code
   * review.
   *
   * @param reviewIFile The <code>IFile</code> implementing class instance.
   */
  private void setReviewIFile(IFile reviewIFile) {
    this.reviewIFile = reviewIFile;
  }

  /**
   * Gets the <code>TargetFile</code> instance.
   *
   * @return the <code>TargetFile</code> instance
   */
  public String getTargetFile() {
    return this.targetFile;
  }

  /**
   * Gets the identification of this code review.
   *
   * @return The identification.
   */
  public String getIssueId() {
    return this.id;
  }

  /**
   * Gets Creation date.
   *
   * @return The <code>Date</code> instance of the creation date.
   */
  public Date getCreationDate() {
    return this.creationDate;
  }

  /**
   * Gets the description of this code review.
   *
   * @return The description.
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Gets the annotation for the review in group review phase.
   *
   * @return the annotation for the review in group review phase.
   */
  public String getAnnotation() {
    return this.annotation;
  }

  /**
   * Gets the revision for the annotation and review in revision phase.
   *
   * @return the revision for the annotation and review in revision phase.
   */
  public String getRevision() {
    return this.revision;
  }

  /**
   * Gets the <code>Resolution</code> instance to hold the  resolution type if it's fix.
   *
   * @return The <code>Resolution</code> instance.
   */
  public Resolution getResolution() {
    return this.resolution;
  }

  /**
   * Gets the modification date.
   *
   * @return The <code>Date</code> instance of the modification date.
   */
  public Date getModificationDate() {
    return this.modificationDate;
  }

  /**
   * Gets the reviewer's name.
   *
   * @return The reviewer's name.
   */
  public String getReviewer() {
    return this.reviewer;
  }

  /**
   * Gets the assignedTo's name.
   *
   * @return the assignedTo's name.
   */
  public String getAssignedTo() {
    return this.assignedTo;
  }

  /**
   * Gets the status if it's solved.
   *
   * @return The <code>Status</code> instance.
   */
  public Status getStatus() {
    return this.status;
  }

  /**
   * Gets the summary of this code review.
   *
   * @return The summary of this code review.
   */
  public String getSummary() {
    return this.summary;
  }

  /**
   * Gets the type if it's defect.
   *
   * @return The <code>Type</code> instance.
   */
  public Type getType() {
    return this.type;
  }

  /**
   * Gets the severity.
   *
   * @return The <code>Severity</code> instance.
   */
  public Severity getSeverity() {
    return this.severity;
  }

  /**
   * Sets creation date.
   *
   * @param creationDate The <code>Date</code> instance.
   */
  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
    // The hash code will be unique (practically hard to generate two same value).
    // To make digits short, 36 radix are used.
    this.id = Long.toString(this.creationDate.getTime(), 36).toUpperCase();
  }

  /**
   * Sets description of this code review.
   *
   * @param description The description of this code review.
   */
  private void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the annotation for the review in group review phase.
   *
   * @param annotation the annotation for the review in group review phase.
   */
  private void setAnnotation(String annotation) {
    this.annotation = annotation;
  }

  /**
   * Sets the revision comment for the annotation and review in revision phase.
   *
   * @param revision the revision comment for the annotation and review in revision phase.
   */
  private void setRevision(String revision) {
    this.revision = revision;
  }

  /**
   * Sets summary of the review.
   *
   * @param summary the summary of review.
   */
  private void setSummary(String summary) {
    this.summary = summary;
  }

  /**
   * Sets the resolution if it's fix.
   *
   * @param resolution The <code>Resolution</code> instance.
   */
  private void setResolution(Resolution resolution) {
    this.resolution = resolution;
  }

  /**
   * Sets the modification date.
   *
   * @param modificationDate The <code>Date</code> instance of the modification date.
   */
  private void setModificationDate(Date modificationDate) {
    this.modificationDate = modificationDate;
  }

  /**
   * Sets the reviewer's name.
   *
   * @param reviewer The reviewer's name.
   *
   * @throws ReviewException if the reviewer's name string is null or empty.
   */
  private void setReviewer(String reviewer) throws ReviewException {
    if ((reviewer == null) || reviewer.equals("")) {
      throw new ReviewException("Please type a reviewer.");
    }
    this.reviewer = reviewer;
  }

  /**
   * Sets the person name who is assigned to.
   *
   * @param assignedTo the person name who is assigned to.
   *
   * @throws ReviewException if the reviewer's name string is null or empty.
   */
  private void setAssignedTo(String assignedTo) throws ReviewException {
    if (assignedTo == null) {
      throw new ReviewException("Please type the person name who is assigned to.");
    }
    this.assignedTo = assignedTo;
  }

  /**
   * Sets the status if it's solved.
   *
   * @param status The <code>Status</code> instance.
   *
   * @throws ReviewException if <code>Status</code> instance is null.
   */
  private void setStatus(Status status) throws ReviewException {
    if (status == null) {
      ReviewModel reviewModel = ReviewModel.getInstance();
      IProject project = reviewModel.getProjectManager().getProject();
      ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
      status = (Status) StatusKeyManager.getInstance(project, reviewId).getItem(0);
    }
    this.status = status;
  }

  /**
   * Sets the type if it's defect.
   *
   * @param type The <code>Type</code> instance.
   *
   * @throws ReviewException if the type is null
   */
  private void setType(Type type) throws ReviewException {
    if (type == null) {
      ReviewModel reviewModel = ReviewModel.getInstance();
      IProject project = reviewModel.getProjectManager().getProject();
      ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
      type = (Type) TypeKeyManager.getInstance(project, reviewId).getItem(0);
    }
    this.type = type;
  }

  /**
   * Sets the severity.
   *
   * @param severity The <code>Severity</code> instance.
   *
   * @throws ReviewException if the severity is null
   */
  private void setSeverity(Severity severity) throws ReviewException {
    if (severity == null) {
      ReviewModel reviewModel = ReviewModel.getInstance();
      IProject project = reviewModel.getProjectManager().getProject();
      ReviewId reviewId = reviewModel.getReviewIdManager().getReviewId();
      SeverityKeyManager severityKeyManager = SeverityKeyManager.getInstance(project, reviewId);
      severity = (Severity) severityKeyManager.getItem(severityKeyManager.size() / 2);
    }
    this.severity = severity;
  }
  
  /**
   * Sets the status of the link to a source code.
   * @param isLinked <code>true</code> if the source code is linked to a source code.
   * Otherwise <code>false</code> should be set.
   */
  public void setLinkStatus(boolean isLinked) {
    this.isLinked = isLinked;
  }
  
  /**
   * Gets the status of the link to a source code. Returns <code>true</code> if 
   * there is the link to a source code. Otherwise returns <code>false</code>.
   * Note that this returns <code>false</code> unless clients set <code>true</code> in
   * the <code>setLinkStatus(boolean)</code> method is initially called.
   * @return the <code>true</code> if there is the link to a source code. 
   * Otherwise returns <code>false</code>.
   */
  public boolean isLinked() {
    return this.isLinked;
  }

  /**
   * Gets the line number of the target file.
   *
   * @return the line number.
   */
  public String getLine() {
    return this.line;
  }

  /**
   * Sets the target file.
   *
   * @param targetFile the target file
   */
  private void setTargetFile(String targetFile) {
    this.targetFile = targetFile;
  }

  /**
   * Sets line number.
   *
   * @param line the line number
   */
  public void setLine(String line) {
    this.line = line;
    this.previousJavaHashCode = this.currentJavaHashCode;
    this.currentJavaHashCode = getJavaHashCode();
  }

  /**
   * Checks if the <code>ReviewIssue</code> instance is equal. Returns <code>true</code> if all the
   * passing variables to this constructor are equal. Returns <code>false</code> otherwise.
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object instanceof ReviewIssue) {
      return (this.hashCode() == object.hashCode());
    }
    return false;
  }

  /**
   * Gets hash code calculated by all the passing variables to this constructor so that the hash
   * code will be different if one of the variables differs.
   *
   * @see java.lang.Object#hashCode()
   */
  public int hashCode() {
    int hash = 17;
    hash = (37 * hash) + ((this.reviewIFile != null) 
                           ? this.reviewIFile.getFullPath().toString().hashCode() 
                           : "".hashCode());
    hash = (37 * hash) + this.form.format(creationDate).hashCode();
    hash = (37 * hash) + this.form.format(modificationDate).hashCode();
    hash = (37 * hash) + this.reviewer.hashCode();
    hash = (37 * hash) + this.assignedTo.hashCode();
    hash = (37 * hash) + this.targetFile.hashCode();
    hash = (37 * hash) + this.line.hashCode();   
    hash = (37 * hash) + this.type.toString().hashCode();
    hash = (37 * hash) + this.severity.toString().hashCode();
    hash = (37 * hash) + this.summary.hashCode();
    hash = (37 * hash) + this.description.hashCode();
    hash = (37 * hash) + this.annotation.hashCode();
    hash = (37 * hash) + this.revision.hashCode();
    hash = (37 * hash) + this.resolution.toString().hashCode();
    hash = (37 * hash) + this.status.toString().hashCode();
    hash = (37 * hash) + String.valueOf(this.isLinked).hashCode();
    return hash;
  }

  /**
   * Checks if the content of <code>ReviewIssue</code> instance is equal. Note that the content
   * means the all field information which are shown in the UI. Returns <code>true</code> if 
   * this content are equal to the passing object. Returns <code>false</code> otherwise.
   * @param object the object to be compared.
   * @return <code>true</code> if the content of <code>ReviewIssue</code> instance is equal.
   */
  public boolean contentEquals(Object object) {
    if (object instanceof ReviewIssue) {
      ReviewIssue reviewIssue = (ReviewIssue) object;
      String fullPath = reviewIssue.getReviewIFile().getFullPath().toString();
      if (this.reviewIFile.getFullPath().toString().equals(fullPath)
          && this.reviewer.equals(reviewIssue.getReviewer())
          && this.assignedTo.equals(reviewIssue.getAssignedTo())
          && this.targetFile.equals(reviewIssue.getTargetFile())
          && this.line.equals(reviewIssue.getLine())
          && this.type.getKey().equals(reviewIssue.getType().getKey())
          && this.severity.getKey().equals(reviewIssue.getSeverity().getKey())
          && this.resolution.getKey().equals(reviewIssue.getResolution().getKey())
          && this.status.getKey().equals(reviewIssue.getStatus().getKey())
          && this.summary.equals(reviewIssue.getSummary())
          && this.description.equals(reviewIssue.getDescription())
          && this.annotation.equals(reviewIssue.getAnnotation())
          && this.revision.equals(reviewIssue.getRevision())) {
        return true;
      }
    }
    return false;
  }
}
