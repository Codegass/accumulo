/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Autogenerated by Thrift Compiler (0.16.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.accumulo.core.tabletserver.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
public class TCompactionStats implements org.apache.thrift.TBase<TCompactionStats, TCompactionStats._Fields>, java.io.Serializable, Cloneable, Comparable<TCompactionStats> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TCompactionStats");

  private static final org.apache.thrift.protocol.TField ENTRIES_READ_FIELD_DESC = new org.apache.thrift.protocol.TField("entriesRead", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField ENTRIES_WRITTEN_FIELD_DESC = new org.apache.thrift.protocol.TField("entriesWritten", org.apache.thrift.protocol.TType.I64, (short)2);
  private static final org.apache.thrift.protocol.TField FILE_SIZE_FIELD_DESC = new org.apache.thrift.protocol.TField("fileSize", org.apache.thrift.protocol.TType.I64, (short)3);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TCompactionStatsStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TCompactionStatsTupleSchemeFactory();

  public long entriesRead; // required
  public long entriesWritten; // required
  public long fileSize; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ENTRIES_READ((short)1, "entriesRead"),
    ENTRIES_WRITTEN((short)2, "entriesWritten"),
    FILE_SIZE((short)3, "fileSize");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // ENTRIES_READ
          return ENTRIES_READ;
        case 2: // ENTRIES_WRITTEN
          return ENTRIES_WRITTEN;
        case 3: // FILE_SIZE
          return FILE_SIZE;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __ENTRIESREAD_ISSET_ID = 0;
  private static final int __ENTRIESWRITTEN_ISSET_ID = 1;
  private static final int __FILESIZE_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ENTRIES_READ, new org.apache.thrift.meta_data.FieldMetaData("entriesRead", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.ENTRIES_WRITTEN, new org.apache.thrift.meta_data.FieldMetaData("entriesWritten", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.FILE_SIZE, new org.apache.thrift.meta_data.FieldMetaData("fileSize", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TCompactionStats.class, metaDataMap);
  }

  public TCompactionStats() {
  }

  public TCompactionStats(
    long entriesRead,
    long entriesWritten,
    long fileSize)
  {
    this();
    this.entriesRead = entriesRead;
    setEntriesReadIsSet(true);
    this.entriesWritten = entriesWritten;
    setEntriesWrittenIsSet(true);
    this.fileSize = fileSize;
    setFileSizeIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TCompactionStats(TCompactionStats other) {
    __isset_bitfield = other.__isset_bitfield;
    this.entriesRead = other.entriesRead;
    this.entriesWritten = other.entriesWritten;
    this.fileSize = other.fileSize;
  }

  public TCompactionStats deepCopy() {
    return new TCompactionStats(this);
  }

  @Override
  public void clear() {
    setEntriesReadIsSet(false);
    this.entriesRead = 0;
    setEntriesWrittenIsSet(false);
    this.entriesWritten = 0;
    setFileSizeIsSet(false);
    this.fileSize = 0;
  }

  public long getEntriesRead() {
    return this.entriesRead;
  }

  public TCompactionStats setEntriesRead(long entriesRead) {
    this.entriesRead = entriesRead;
    setEntriesReadIsSet(true);
    return this;
  }

  public void unsetEntriesRead() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __ENTRIESREAD_ISSET_ID);
  }

  /** Returns true if field entriesRead is set (has been assigned a value) and false otherwise */
  public boolean isSetEntriesRead() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __ENTRIESREAD_ISSET_ID);
  }

  public void setEntriesReadIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __ENTRIESREAD_ISSET_ID, value);
  }

  public long getEntriesWritten() {
    return this.entriesWritten;
  }

  public TCompactionStats setEntriesWritten(long entriesWritten) {
    this.entriesWritten = entriesWritten;
    setEntriesWrittenIsSet(true);
    return this;
  }

  public void unsetEntriesWritten() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __ENTRIESWRITTEN_ISSET_ID);
  }

  /** Returns true if field entriesWritten is set (has been assigned a value) and false otherwise */
  public boolean isSetEntriesWritten() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __ENTRIESWRITTEN_ISSET_ID);
  }

  public void setEntriesWrittenIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __ENTRIESWRITTEN_ISSET_ID, value);
  }

  public long getFileSize() {
    return this.fileSize;
  }

  public TCompactionStats setFileSize(long fileSize) {
    this.fileSize = fileSize;
    setFileSizeIsSet(true);
    return this;
  }

  public void unsetFileSize() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __FILESIZE_ISSET_ID);
  }

  /** Returns true if field fileSize is set (has been assigned a value) and false otherwise */
  public boolean isSetFileSize() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __FILESIZE_ISSET_ID);
  }

  public void setFileSizeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __FILESIZE_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case ENTRIES_READ:
      if (value == null) {
        unsetEntriesRead();
      } else {
        setEntriesRead((java.lang.Long)value);
      }
      break;

    case ENTRIES_WRITTEN:
      if (value == null) {
        unsetEntriesWritten();
      } else {
        setEntriesWritten((java.lang.Long)value);
      }
      break;

    case FILE_SIZE:
      if (value == null) {
        unsetFileSize();
      } else {
        setFileSize((java.lang.Long)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case ENTRIES_READ:
      return getEntriesRead();

    case ENTRIES_WRITTEN:
      return getEntriesWritten();

    case FILE_SIZE:
      return getFileSize();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case ENTRIES_READ:
      return isSetEntriesRead();
    case ENTRIES_WRITTEN:
      return isSetEntriesWritten();
    case FILE_SIZE:
      return isSetFileSize();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof TCompactionStats)
      return this.equals((TCompactionStats)that);
    return false;
  }

  public boolean equals(TCompactionStats that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_entriesRead = true;
    boolean that_present_entriesRead = true;
    if (this_present_entriesRead || that_present_entriesRead) {
      if (!(this_present_entriesRead && that_present_entriesRead))
        return false;
      if (this.entriesRead != that.entriesRead)
        return false;
    }

    boolean this_present_entriesWritten = true;
    boolean that_present_entriesWritten = true;
    if (this_present_entriesWritten || that_present_entriesWritten) {
      if (!(this_present_entriesWritten && that_present_entriesWritten))
        return false;
      if (this.entriesWritten != that.entriesWritten)
        return false;
    }

    boolean this_present_fileSize = true;
    boolean that_present_fileSize = true;
    if (this_present_fileSize || that_present_fileSize) {
      if (!(this_present_fileSize && that_present_fileSize))
        return false;
      if (this.fileSize != that.fileSize)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(entriesRead);

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(entriesWritten);

    hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(fileSize);

    return hashCode;
  }

  @Override
  public int compareTo(TCompactionStats other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetEntriesRead(), other.isSetEntriesRead());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEntriesRead()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.entriesRead, other.entriesRead);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetEntriesWritten(), other.isSetEntriesWritten());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEntriesWritten()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.entriesWritten, other.entriesWritten);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetFileSize(), other.isSetFileSize());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetFileSize()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.fileSize, other.fileSize);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TCompactionStats(");
    boolean first = true;

    sb.append("entriesRead:");
    sb.append(this.entriesRead);
    first = false;
    if (!first) sb.append(", ");
    sb.append("entriesWritten:");
    sb.append(this.entriesWritten);
    first = false;
    if (!first) sb.append(", ");
    sb.append("fileSize:");
    sb.append(this.fileSize);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TCompactionStatsStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TCompactionStatsStandardScheme getScheme() {
      return new TCompactionStatsStandardScheme();
    }
  }

  private static class TCompactionStatsStandardScheme extends org.apache.thrift.scheme.StandardScheme<TCompactionStats> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TCompactionStats struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ENTRIES_READ
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.entriesRead = iprot.readI64();
              struct.setEntriesReadIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ENTRIES_WRITTEN
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.entriesWritten = iprot.readI64();
              struct.setEntriesWrittenIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // FILE_SIZE
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.fileSize = iprot.readI64();
              struct.setFileSizeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TCompactionStats struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(ENTRIES_READ_FIELD_DESC);
      oprot.writeI64(struct.entriesRead);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(ENTRIES_WRITTEN_FIELD_DESC);
      oprot.writeI64(struct.entriesWritten);
      oprot.writeFieldEnd();
      oprot.writeFieldBegin(FILE_SIZE_FIELD_DESC);
      oprot.writeI64(struct.fileSize);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TCompactionStatsTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TCompactionStatsTupleScheme getScheme() {
      return new TCompactionStatsTupleScheme();
    }
  }

  private static class TCompactionStatsTupleScheme extends org.apache.thrift.scheme.TupleScheme<TCompactionStats> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TCompactionStats struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetEntriesRead()) {
        optionals.set(0);
      }
      if (struct.isSetEntriesWritten()) {
        optionals.set(1);
      }
      if (struct.isSetFileSize()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetEntriesRead()) {
        oprot.writeI64(struct.entriesRead);
      }
      if (struct.isSetEntriesWritten()) {
        oprot.writeI64(struct.entriesWritten);
      }
      if (struct.isSetFileSize()) {
        oprot.writeI64(struct.fileSize);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TCompactionStats struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.entriesRead = iprot.readI64();
        struct.setEntriesReadIsSet(true);
      }
      if (incoming.get(1)) {
        struct.entriesWritten = iprot.readI64();
        struct.setEntriesWrittenIsSet(true);
      }
      if (incoming.get(2)) {
        struct.fileSize = iprot.readI64();
        struct.setFileSizeIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
  private static void unusedMethod() {}
}

