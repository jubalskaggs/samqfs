/* Definitions for communicating with a remote tape drive.
   Copyright (C) 1988, 1992, 1996, 1997 Free Software Foundation, Inc.

   This program is free software; you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation; either version 2, or (at your option)
   any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software Foundation,
   Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.  */

/* For the avoidance of doubt, except that if any license choice other
   than GPL or LGPL is available it will apply instead, Sun elects to
   use only the General Public License version 2 (GPLv2) at this time
   for any software where a choice of GPL license versions is made
   available with the language indicating that GPLv2 or any later
   version may be used, or where a choice of which version of the GPL
   is applied is otherwise unspecified. */

extern char *rmt_path__;

int rmt_open__ PARAMS ((const char *, int, int, const char *));
int rmt_close__ PARAMS ((int));
int rmt_read__ PARAMS ((int, char *, unsigned int));
int rmt_write__ PARAMS ((int, char *, unsigned int));
long rmt_lseek__ PARAMS ((int, off_t, int));
int rmt_ioctl__ PARAMS ((int, int, char *));

/* A filename is remote if it contains a colon not preceeded by a slash,
   to take care of `/:/' which is a shorthand for `/.../<CELL-NAME>/fs'
   on machines running OSF's Distributing Computing Environment (DCE) and
   Distributed File System (DFS).  However, when --force-local, a
   filename is never remote.  */

#define _remdev(Path) \
  (!force_local_option && (rmt_path__ = strchr (Path, ':')) \
   && rmt_path__ > (Path) && rmt_path__[-1] != '/')

#define _isrmt(Fd) \
  ((Fd) >= __REM_BIAS)

#define __REM_BIAS 128

#ifndef O_CREAT
# define O_CREAT 01000
#endif

#define rmtopen(Path, Oflag, Mode, Command) \
  (_remdev (Path) ? rmt_open__ (Path, Oflag, __REM_BIAS, Command) \
   : open (Path, Oflag, Mode))

#define rmtaccess(Path, Amode) \
  (_remdev (Path) ? 0 : access (Path, Amode))

#define rmtstat(Path, Buffer) \
  (_remdev (Path) ? (errno = EOPNOTSUPP), -1 : stat (Path, Buffer))
				/* FIXME: errno should be read-only */

#define rmtcreat(Path, Mode, Command) \
   (_remdev (Path) \
    ? rmt_open__ (Path, 1 | O_CREAT, __REM_BIAS, Command) \
    : open (Path, O_WRONLY | O_CREAT | O_TRUNC | SAM_O_LARGEFILE, Mode))

#define rmtlstat(Path, Buffer) \
  (_remdev (Path) ? (errno = EOPNOTSUPP), -1 : lstat (Path, Buffer))
				/* FIXME: errno should be read-only */

#define rmtread(Fd, Buffer, Length) \
  (_isrmt (Fd) ? rmt_read__ (Fd - __REM_BIAS, Buffer, Length) \
   : read (Fd, Buffer, Length))

#define rmtwrite(Fd, Buffer, Length) \
  (_isrmt (Fd) ? rmt_write__ (Fd - __REM_BIAS, Buffer, Length) \
   : write (Fd, Buffer, Length))

#define rmtlseek(Fd, Offset, Where) \
  (_isrmt (Fd) ? rmt_lseek__ (Fd - __REM_BIAS, Offset, Where) \
   : lseek (Fd, Offset, Where))

#define rmtclose(Fd) \
  (_isrmt (Fd) ? rmt_close__ (Fd - __REM_BIAS) : close (Fd))

#define rmtioctl(Fd, Request, Argument) \
  (_isrmt (Fd) ? rmt_ioctl__ (Fd - __REM_BIAS, Request, Argument) \
   : ioctl (Fd, Request, Argument))

#define rmtdup(Fd) \
  (_isrmt (Fd) ? (errno = EOPNOTSUPP), -1 : dup (Fd))
				/* FIXME: errno should be read-only */

#define rmtfstat(Fd, Buffer) \
  (_isrmt (Fd) ? (errno = EOPNOTSUPP), -1 : fstat (Fd, Buffer))
				/* FIXME: errno should be read-only */

#define rmtfcntl(Fd, Command, Argument) \
  (_isrmt (Fd) ? (errno = EOPNOTSUPP), -1 : fcntl (Fd, Command, Argument))
				/* FIXME: errno should be read-only */

#define rmtisatty(Fd) \
  (_isrmt (Fd) ? 0 : isatty (Fd))
